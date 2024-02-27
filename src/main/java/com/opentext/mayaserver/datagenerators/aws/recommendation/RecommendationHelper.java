package com.opentext.mayaserver.datagenerators.aws.recommendation;

import com.opentext.mayaserver.config.ApplicationPropertiesConfig;
import com.opentext.mayaserver.datagenerators.aws.recommendation.model.AwsLookbackPeriod;
import com.opentext.mayaserver.datagenerators.aws.recommendation.model.AwsSavingsPlanRequest;
import com.opentext.mayaserver.datagenerators.aws.recommendation.model.AwsSavingsPlanRequestAccountScope;
import com.opentext.mayaserver.datagenerators.aws.recommendation.model.AwsSavingsPlanRequestPaymentOption;
import com.opentext.mayaserver.datagenerators.aws.recommendation.model.AwsSavingsPlanRequestPlanType;
import com.opentext.mayaserver.datagenerators.aws.recommendation.model.AwsSavingsPlanRequestTerm;
import com.opentext.mayaserver.datagenerators.aws.recommendation.model.RecommendationHolder;
import com.opentext.mayaserver.datagenerators.aws.recommendation.model.ResponseRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.opentext.mayaserver.utility.Constants.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class RecommendationHelper {

    private final ApplicationPropertiesConfig applicationPropertiesConfig;

    public List<RecommendationHolder> getRecommendationHolders(String useCaseName) {
        List<RecommendationHolder> result = getAwsSpRecommendationHolders(useCaseName);
        result.add(getAwsSpUtilizationHolder(useCaseName));
        return result;
    }

    private List<RecommendationHolder> getAwsSpRecommendationHolders(String useCaseName) {
        List<RecommendationHolder> recommendationHolders = new ArrayList<>();

        int index = 0;
        for (AwsSavingsPlanRequest spRequest : getAwsSavingsPlanRequestList()) {
            String name = getRecommendationName(spRequest);
            String nextPageName = name + "-p2";
            boolean hasNextPage = spRequest.getAccountScope() == AwsSavingsPlanRequestAccountScope.LINKED
                            && spRequest.getAwsSavingsPlanRequestPlanType() == AwsSavingsPlanRequestPlanType.EC2_INSTANCE_SP
                            && spRequest.getAwsLookbackPeriod() == AwsLookbackPeriod.SEVEN_DAYS;
            recommendationHolders.add(new RecommendationHolder(
                    name,
                    getDescription(spRequest, ++index, ""),
                    createrAwsSpRecommendationHeadersMap(),
                    createAwsSpRecommendationRules(spRequest, null),
                    AWS_SP_RECOMMENDATION,
                    getRecommendationDataFilePath(useCaseName, SP_RECOMMENDATION_PREFIX + name + JSON)
            ));
            if (hasNextPage) {
                recommendationHolders.add(new RecommendationHolder(
                        nextPageName,
                        getDescription(spRequest, index, " Page 2"),
                        createrAwsSpRecommendationHeadersMap(),
                        createAwsSpRecommendationRules(spRequest, getNextPageToken(nextPageName)),
                        AWS_SP_RECOMMENDATION,
                        getRecommendationDataFilePath(useCaseName, getNextPageToken(nextPageName))
                ));
            }
        }
        return recommendationHolders;
    }

    private List<AwsSavingsPlanRequest> getAwsSavingsPlanRequestList() {
        List<AwsSavingsPlanRequest> awsSavingsPlanRequests = new ArrayList<>();
        for (AwsSavingsPlanRequestAccountScope accountScope : AwsSavingsPlanRequestAccountScope.enabled()) {
            for (AwsLookbackPeriod lookback : AwsLookbackPeriod.enabled()) {
                for (AwsSavingsPlanRequestPaymentOption paymentOption : AwsSavingsPlanRequestPaymentOption.enabled()) {
                    for (AwsSavingsPlanRequestPlanType planType : AwsSavingsPlanRequestPlanType.enabled()) {
                        for (AwsSavingsPlanRequestTerm planTerm : AwsSavingsPlanRequestTerm.enabled()) {
                            awsSavingsPlanRequests.add(new AwsSavingsPlanRequest(accountScope, lookback, paymentOption, planType, planTerm));
                        }
                    }
                }
            }
        }
        return awsSavingsPlanRequests;
    }

    private RecommendationHolder getAwsSpUtilizationHolder(String useCaseName) {
        String name = "monthly-utilization";
        return new RecommendationHolder(
                name,
                "AWS AP Utilization",
                createrAwsSpUtilizationHeadersMap(),
                createAwsSpUtilizationRules(),
                AWS_SP_UTILIZATION,
                getRecommendationDataFilePath(useCaseName, SP_UTILIZATION_PREFIX + name + JSON)
        );
    }

    private Map<String, String> createrAwsSpRecommendationHeadersMap() {
        Map<String, String> result = new HashMap<>();
        result.put(X_AMZ_TARGET, AWS_SP_RECOMMENDATION_API_TARGET);
        return result;
    }

    private Map<String, String> createrAwsSpUtilizationHeadersMap() {
        Map<String, String> result = new HashMap<>();
        result.put(X_AMZ_TARGET, AWS_SP_UTILIZATION_API_TARGET);
        return result;
    }

    private String getRecommendationName(AwsSavingsPlanRequest spRequest) {
        return spRequest.getAccountScope().getShortName() + DASH
                + spRequest.getAwsLookbackPeriod().getShortName() + DASH
                + spRequest.getAwsSavingsPlanRequestPaymentOption().getShortName() + DASH
                + spRequest.getAwsSavingsPlanRequestPlanType().getShortName() + DASH
                + spRequest.getAwsSavingsPlanRequestTerm().getShortName();
    }

    private String getDescription(AwsSavingsPlanRequest spRequest,
                                  int index,
                                  String extraText) {

        return "AWS SP Recommendation " + index + SPACED_DASH
                + spRequest.getAccountScope().name() + SPACED_DASH
                + spRequest.getAwsLookbackPeriod().name() + SPACED_DASH
                + spRequest.getAwsSavingsPlanRequestPaymentOption().name() + SPACED_DASH
                + spRequest.getAwsSavingsPlanRequestPlanType().name() + SPACED_DASH
                + spRequest.getAwsSavingsPlanRequestTerm().name() + extraText;
    }

    private String getNextPageToken(String nextPageName) {
        return SP_RECOMMENDATION_PREFIX + nextPageName + JSON;
    }

    private List<ResponseRule> createAwsSpRecommendationRules(AwsSavingsPlanRequest spRequest,
                                                              String nextPageToken) {
        List<ResponseRule> rules = new ArrayList<>();
        rules.add(new ResponseRule("header", X_AMZ_TARGET, AWS_SP_RECOMMENDATION_API_TARGET, "equals"));
        rules.add(new ResponseRule("body", "AccountScope", spRequest.getAccountScope().name(), "equals"));
        rules.add(new ResponseRule("body", "LookbackPeriodInDays", spRequest.getAwsLookbackPeriod().name(), "equals"));
        rules.add(new ResponseRule("body", "PaymentOption", spRequest.getAwsSavingsPlanRequestPaymentOption().name(), "equals"));
        rules.add(new ResponseRule("body", "SavingsPlansType", spRequest.getAwsSavingsPlanRequestPlanType().name(), "equals"));
        rules.add(new ResponseRule("body", "TermInYears", spRequest.getAwsSavingsPlanRequestTerm().name(), "equals"));
        rules.add(new ResponseRule("body", "NextPageToken", nextPageToken, (nextPageToken == null)? null : "equals"));
        return rules;
    }

    private List<ResponseRule> createAwsSpUtilizationRules() {
        List<ResponseRule> rules = new ArrayList<>();
        rules.add(new ResponseRule("header", X_AMZ_TARGET, AWS_SP_UTILIZATION_API_TARGET, "equals"));
        return rules;
    }

    private String getRecommendationDataFilePath(String useCaseName, String fileName) {
        String nfsDataPath = applicationPropertiesConfig.getRecommendation().getNfsDataPath();
        return nfsDataPath.endsWith(FILE_PATH_SEPARATOR)?
                nfsDataPath + useCaseName + FILE_PATH_SEPARATOR + fileName :
                nfsDataPath + FILE_PATH_SEPARATOR + useCaseName + FILE_PATH_SEPARATOR + fileName;
    }
}
