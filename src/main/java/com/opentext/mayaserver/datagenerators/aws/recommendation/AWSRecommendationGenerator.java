package com.opentext.mayaserver.datagenerators.aws.recommendation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.opentext.mayaserver.datagenerators.aws.recommendation.model.AwsLookbackPeriod;
import com.opentext.mayaserver.datagenerators.aws.recommendation.model.RecommendationHolder;
import com.opentext.mayaserver.datagenerators.util.DateUtils;
import com.opentext.mayaserver.exceptions.MayaServerException;
import com.opentext.mayaserver.models.vo.AccountDataVO;
import com.opentext.mayaserver.services.AwsRecommendationSeedDataService;
import com.opentext.mayaserver.utility.CommonUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.opentext.mayaserver.utility.Constants.*;

@RequiredArgsConstructor
@Slf4j
@Component
public class AWSRecommendationGenerator {

    private final AwsRecommendationSeedDataService awsRecommendationSeedDataService;

    public void generateAwsSpRecommendationApiResponses(AccountDataVO accountData, List<RecommendationHolder> recommendationHolders, boolean isDemoModeEnabled) {
        Map<String, CircularList> idConversionMap = createIdConversionMap(accountData, isDemoModeEnabled);

        for (RecommendationHolder recommendationHolder: recommendationHolders) {
            String fileName = recommendationHolder.getName() + JSON;
            try {
                String fileContent = CommonUtils.readFileFromResource(AWS_SP_RECOMMENDATION_RESOURCE_PATH, fileName);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(fileContent);
                updateRecommendationResponseData(jsonNode, DateUtils.getCurrentUtcDateTimeString(), isDemoModeEnabled,
                        idConversionMap, accountData, DEMO_RECOMMENDATION_SKIP_LIST.contains(recommendationHolder.getName()));

                File updatedFile = new File(recommendationHolder.getDataFilePath());
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(updatedFile, jsonNode);
            } catch (IOException e) {
                log.error("Failed to process recommendation data file {}", fileName);
                throw new MayaServerException(e.getMessage());
            }
        }
        log.debug("Saving plans recommendation data files Created Successfully");
    }

    private Map<String, CircularList> createIdConversionMap(AccountDataVO accountData, boolean isDemoModeEnabled) {
        Map<String, CircularList> idConversionMap = new HashMap<>();
        if (!isDemoModeEnabled) {
            awsRecommendationSeedDataService.listValidRootAccountIds().forEach(seedRootAccountId -> idConversionMap.put(seedRootAccountId, new CircularList(Arrays.asList(accountData.getRootAccount()))));
            List<String> allMemberAccountIds = awsRecommendationSeedDataService.listAllMemberAccountIds();
            List<String> dataMemberAccountIdList = accountData.getMemberAccounts();
            if (allMemberAccountIds.size() >= dataMemberAccountIdList.size()) {
                for (int seedAccountIdIndex = 0; seedAccountIdIndex < allMemberAccountIds.size(); seedAccountIdIndex++) {
                    int dataAccountIdIndex = seedAccountIdIndex % dataMemberAccountIdList.size();
                    idConversionMap.put(allMemberAccountIds.get(seedAccountIdIndex), new CircularList(Arrays.asList(dataMemberAccountIdList.get(dataAccountIdIndex))));
                }
            } else {
                ArrayList<String>[] accountIdListArray = new ArrayList[allMemberAccountIds.size()];
                Arrays.setAll(accountIdListArray, element -> new ArrayList<>());
                for (int dataAccountIdIndex = 0; dataAccountIdIndex < dataMemberAccountIdList.size(); dataAccountIdIndex++) {
                    int seedAccountIdIndex = dataAccountIdIndex % allMemberAccountIds.size();
                    accountIdListArray[seedAccountIdIndex].add(dataMemberAccountIdList.get(dataAccountIdIndex));
                }
                for (int seedAccountIdIndex = 0; seedAccountIdIndex < allMemberAccountIds.size(); seedAccountIdIndex++) {
                    idConversionMap.put(allMemberAccountIds.get(seedAccountIdIndex), new CircularList(accountIdListArray[seedAccountIdIndex]));
                }
            }
        }
        return idConversionMap;
    }

    public void generateAwsSpUtilizationApiResponses(List<RecommendationHolder> recommendationHolders) {
        for (RecommendationHolder recommendationHolder: recommendationHolders) {
            String fileName = recommendationHolder.getName() + JSON;
            try {
                String fileContent = CommonUtils.readFileFromResource(AWS_SP_UTILIZATION_RESOURCE_PATH, fileName);
                log.debug("Creating utilization data file: {}", recommendationHolder);
                File updatedFile = new File(recommendationHolder.getDataFilePath());
                FileUtils.writeStringToFile(updatedFile, fileContent);
            } catch (IOException e) {
                log.error("Failed to process utilization data file {}", fileName);
                throw new MayaServerException(e.getMessage());
            }
        }
        log.debug("Utilization data file Created Successfully");
    }

    private void updateRecommendationResponseData(JsonNode jsonNode, String dateTimeString, boolean isDemoModeEnabled,
                                                  Map<String, CircularList> idConversionMap, AccountDataVO accountData, boolean skipDemoRecommendation) {
        ObjectNode rootNode = (ObjectNode) jsonNode;
        ObjectNode metadata = (ObjectNode) rootNode.get("Metadata");
        if (metadata != null) {
            metadata.put("GenerationTimestamp", dateTimeString);
            metadata.put("RecommendationId", UUID.randomUUID().toString());
        }
        ObjectNode spRecommendation = (ObjectNode) rootNode.get("SavingsPlansPurchaseRecommendation");
        String LookbackPeriodString = spRecommendation.get("LookbackPeriodInDays").asText();
        ArrayNode spRecommendationDetailsArray = (ArrayNode) spRecommendation.get("SavingsPlansPurchaseRecommendationDetails");
        if (isDemoModeEnabled) {
            List<String> accountIdsForCurrentUseCase = new ArrayList<>(accountData.getMemberAccounts());
            accountIdsForCurrentUseCase.add(accountData.getRootAccount());
            SpRecommendationSummary spRecommendationSummary = new SpRecommendationSummary(LookbackPeriodString);
            for (final Iterator<JsonNode> i = spRecommendationDetailsArray.elements(); i.hasNext(); ) {
                final ObjectNode detailsNode = (ObjectNode) i.next();
                if (!skipDemoRecommendation && accountIdsForCurrentUseCase.contains(detailsNode.get("AccountId").asText())) {
                    addToSummary(detailsNode, spRecommendationSummary);
                } else {
                    i.remove();
                }
            }
            updateSummaryNode((ObjectNode) spRecommendation.get("SavingsPlansPurchaseRecommendationSummary"), spRecommendationSummary);
        } else {
            for (JsonNode SpRecommendationDetails : spRecommendationDetailsArray) {
                ObjectNode detailsNode = (ObjectNode) SpRecommendationDetails;
                convertAccountIds(detailsNode, isDemoModeEnabled, idConversionMap);
                detailsNode.put("RecommendationDetailId", UUID.randomUUID().toString());
                ObjectNode spDetailsNode = (ObjectNode) detailsNode.get("SavingsPlansDetails");
                if (spDetailsNode != null) {
                    spDetailsNode.put("OfferingId", UUID.randomUUID().toString());
                }
            }
        }
    }

    private void addToSummary(ObjectNode detailsNode, SpRecommendationSummary spRecommendationSummary) {
        spRecommendationSummary.addEstimatedMonthlySavingsAmount(getDoubleValue(detailsNode, "EstimatedMonthlySavingsAmount"));
        spRecommendationSummary.addEstimatedOnDemandCostWithCurrentCommitment(getDoubleValue(detailsNode, "EstimatedOnDemandCostWithCurrentCommitment"));
        spRecommendationSummary.addEstimatedSavingsAmount(getDoubleValue(detailsNode, "EstimatedSavingsAmount"));
        spRecommendationSummary.addHourlyCommitmentToPurchase(getDoubleValue(detailsNode, "HourlyCommitmentToPurchase"));
        spRecommendationSummary.addCurrentAverageHourlyOnDemandSpend(getDoubleValue(detailsNode, "CurrentAverageHourlyOnDemandSpend"));
        spRecommendationSummary.incrementCount();
    }

    private void updateSummaryNode(ObjectNode summaryNode, SpRecommendationSummary spRecommendationSummary) {
        summaryNode.put("CurrentOnDemandSpend", String.valueOf(spRecommendationSummary.getCurrentOnDemandSpend()));
        summaryNode.put("DailyCommitmentToPurchase", String.valueOf(spRecommendationSummary.getDailyCommitmentToPurchase()));
        summaryNode.put("EstimatedMonthlySavingsAmount", String.valueOf(spRecommendationSummary.getEstimatedMonthlySavingsAmount()));
        summaryNode.put("EstimatedOnDemandCostWithCurrentCommitment", String.valueOf(spRecommendationSummary.getEstimatedOnDemandCostWithCurrentCommitment()));
        summaryNode.put("EstimatedROI", String.valueOf(spRecommendationSummary.getEstimatedROI()));
        summaryNode.put("EstimatedSavingsAmount", String.valueOf(spRecommendationSummary.getEstimatedSavingsAmount()));
        summaryNode.put("EstimatedSavingsPercentage", String.valueOf(spRecommendationSummary.getEstimatedSavingsPercentage()));
        summaryNode.put("EstimatedTotalCost", String.valueOf(spRecommendationSummary.getEstimatedTotalCost()));
        summaryNode.put("HourlyCommitmentToPurchase", String.valueOf(spRecommendationSummary.getHourlyCommitmentToPurchase()));
        summaryNode.put("TotalRecommendationCount", String.valueOf(spRecommendationSummary.getTotalRecommendationCount()));
    }

    private double getDoubleValue(ObjectNode detailsNode, String field) {
        JsonNode node = detailsNode.get(field);
        return (node != null)? node.asDouble() : 0.0;
    }

    private void convertAccountIds(ObjectNode detailsNode, boolean isDemoModeEnabled, Map<String, CircularList> idConversionMap) {
        if (isDemoModeEnabled) {
            return;
        }
        String accountId = (detailsNode.get(ACCOUNT_ID) != null)? detailsNode.get(ACCOUNT_ID).textValue() : "";
        String actualRootAccountId = idConversionMap.get(accountId).getCurrent();

        if (actualRootAccountId != null) {
            detailsNode.put(ACCOUNT_ID, actualRootAccountId);
        } else {
            log.warn("convertAccountIds - unable to convert account id {}", accountId);
        }
    }

    private class CircularList {
        private List<String> data;
        private int currentIndex;

        public CircularList(List<String> data) {
            assert (data != null && data.size() > 0);
            this.data = data;
            this.currentIndex = 0;
        }

        public String getCurrent() {
            String result = data.get(currentIndex);
            if (currentIndex < data.size() - 1) {
                currentIndex++;
            } else {
                currentIndex = 0;
            }
            return result;
        }
    }

    @Getter
    private class SpRecommendationSummary {
        private double currentAverageHourlyOnDemandSpend;
        private double estimatedMonthlySavingsAmount;
        private double estimatedOnDemandCostWithCurrentCommitment;
        private double estimatedSavingsAmount;
        private double hourlyCommitmentToPurchase;
        private int totalRecommendationCount;
        private final double lookbackPeriodInDays;

        public SpRecommendationSummary(String lookbackPeriodString) {
             currentAverageHourlyOnDemandSpend = 0.0;
             estimatedMonthlySavingsAmount = 0.0;
             estimatedOnDemandCostWithCurrentCommitment = 0.0;
             estimatedSavingsAmount = 0.0;
             hourlyCommitmentToPurchase = 0.0;
             totalRecommendationCount = 0;
            switch (AwsLookbackPeriod.valueOf(lookbackPeriodString)) {
                case SEVEN_DAYS -> this.lookbackPeriodInDays = 7.0;
                case THIRTY_DAYS -> this.lookbackPeriodInDays = 30.0;
                case SIXTY_DAYS -> this.lookbackPeriodInDays = 60.0;
                default -> this.lookbackPeriodInDays = 30.0;
            }
        }

        private void addCurrentAverageHourlyOnDemandSpend(double addAmount) {
            currentAverageHourlyOnDemandSpend += addAmount;
        }

        private void addEstimatedMonthlySavingsAmount(double addAmount) {
            estimatedMonthlySavingsAmount += addAmount;
        }

        private void addEstimatedOnDemandCostWithCurrentCommitment(double addAmount) {
            estimatedOnDemandCostWithCurrentCommitment += addAmount;
        }

        private void addEstimatedSavingsAmount(double addAmount) {
            estimatedSavingsAmount += addAmount;
        }

        private void addHourlyCommitmentToPurchase(double addAmount) {
            hourlyCommitmentToPurchase += addAmount;
        }

        private void incrementCount() {
            totalRecommendationCount++;
        }

        public double getCurrentOnDemandSpend() {
            return currentAverageHourlyOnDemandSpend * 24.0 * lookbackPeriodInDays;
        }
        private int getTotalRecommendationCount() {
            return totalRecommendationCount;
        }

        private double getDailyCommitmentToPurchase() {
            return hourlyCommitmentToPurchase * 24.0;
        }

        private double getEstimatedROI() {
            return (hourlyCommitmentToPurchase == 0.0)? 0.0 : estimatedSavingsAmount / hourlyCommitmentToPurchase / 24.0 / lookbackPeriodInDays;
        }

        private double getEstimatedSavingsPercentage() {
            return (estimatedOnDemandCostWithCurrentCommitment == 0.0)? 0.0 : 100.0 * estimatedSavingsAmount / estimatedOnDemandCostWithCurrentCommitment;
        }

        private double getEstimatedTotalCost() {
            return estimatedOnDemandCostWithCurrentCommitment - estimatedSavingsAmount;
        }
    }
}
