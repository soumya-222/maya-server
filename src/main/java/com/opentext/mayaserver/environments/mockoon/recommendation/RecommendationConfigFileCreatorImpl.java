package com.opentext.mayaserver.environments.mockoon.recommendation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.opentext.mayaserver.config.ApplicationPropertiesConfig;
import com.opentext.mayaserver.datagenerators.aws.recommendation.RecommendationHelper;
import com.opentext.mayaserver.datagenerators.aws.recommendation.model.RecommendationHolder;
import com.opentext.mayaserver.datagenerators.aws.recommendation.model.ResponseRule;
import com.opentext.mayaserver.environments.ConfigUtils;
import com.opentext.mayaserver.environments.K8sObjectsManager;
import com.opentext.mayaserver.exceptions.MayaServerException;
import com.opentext.mayaserver.models.UseCase;
import com.opentext.mayaserver.repository.UseCaseRepository;
import com.opentext.mayaserver.services.UseCaseAPIService;
import com.opentext.mayaserver.utility.CommonUtils;
import com.opentext.mayaserver.utility.ProfileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static com.opentext.mayaserver.utility.Constants.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class RecommendationConfigFileCreatorImpl implements RecommendationConfigFileCreator {

    private final UseCaseRepository useCaseRepository;
    private final ApplicationPropertiesConfig applicationPropertiesConfig;
    private final K8sObjectsManager K8sObjectsManager;
    private final RecommendationHelper recommendationHelper;
    private UseCaseAPIService useCaseAPIService;
    private final ProfileHandler profileHandler;

    @Autowired
    public void setUseCaseAPIService(@Lazy UseCaseAPIService useCaseAPIService) {
        this.useCaseAPIService = useCaseAPIService;
    }

    @Override
    public void prepareConfigFile(String useCaseName) {
        log.debug("Preparing recommendation config file, current status {}", useCaseAPIService.getUseCaseByNameOrId(useCaseName).getState().name());
        if (profileHandler.isProductionProfileActive()) {
            try {
                String configFilePath = CommonUtils.createDirectoryBasedOnUseCase(useCaseName, applicationPropertiesConfig.getRecommendation().getNfsConfigPath());
                String fileContent = CommonUtils.readFileFromResource(RECOMMENDATION_RESOURCE_PATH,RECOMMENDATION_TEMPLATE_FILE_NAME + JSON);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(fileContent);
                UseCase useCase = useCaseRepository.findByUseCaseName(useCaseName).get();
                JsonNode updatedJsonNode = addDynamicResponseBlocks(jsonNode, recommendationHelper.getRecommendationHolders(useCaseName));
                if (updatedJsonNode != null) {
                    jsonNode = updatedJsonNode;
                }

                if (jsonNode != null) {
                    ConfigUtils.traverseAndUpdate(jsonNode, useCase);
                }

                File updatedFile = new File(configFilePath + File.separator + RECOMMENDATION_TEMPLATE_FILE_NAME + "_" + useCaseName + JSON);
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(updatedFile, jsonNode);
                log.info("Recommendation Config file Created Successfully");
                K8sObjectsManager.prepareRecommendationYaml(configFilePath, useCase);
                log.debug("Recommendation Yaml File Updated Successfully");

            } catch (Exception e) {
                log.error("Unable to create recommendation configuration file {}", useCaseName);
                throw new MayaServerException(e.getMessage());
            }
        }
    }

    private JsonNode addDynamicResponseBlocks(JsonNode jsonNode, List<RecommendationHolder> recommendationHolders) {
        ObjectNode objectNode = (ObjectNode) jsonNode;
        ArrayNode responseArray = (ArrayNode) objectNode.get("routes").get(0).get("responses");
        for (RecommendationHolder recommendationHolder: recommendationHolders) {
            responseArray.add(createDynamicResponseBlock(recommendationHolder));
        }
        responseArray.add(createSpRecommendationExceptionBlock());
        responseArray.add(createUnknownOperationExceptionBlock());
        return jsonNode;
    }

    private ObjectNode createDynamicResponseBlock(RecommendationHolder recommendationHolder) {
        ObjectNode dynamicBlock = new ObjectMapper().createObjectNode();
        dynamicBlock.put("uuid", UUID.randomUUID().toString());
        dynamicBlock.put("body", "{}");
        dynamicBlock.put("latency", 0);
        dynamicBlock.put("statusCode", 200);
        dynamicBlock.put("label", recommendationHolder.getDescription());

        ArrayNode headersArray = dynamicBlock.putArray("headers");
        recommendationHolder.getHeaders().forEach((key, value) -> {
            ObjectNode headerObject = headersArray.addObject();
            headerObject.put("key", key);
            headerObject.put("value", value);
        });

        dynamicBlock.put("bodyType", "FILE");
        dynamicBlock.put("filePath", recommendationHolder.getDataFilePath());
        dynamicBlock.put("databucketID", "");
        dynamicBlock.put("sendFileAsBody", (recommendationHolder.getEndpointType() == AWS_SP_RECOMMENDATION)? true : false);

        ArrayNode rulesArray = dynamicBlock.putArray("rules");
        for (ResponseRule rule: recommendationHolder.getRules()) {
            ObjectNode ruleObject = rulesArray.addObject();
            ruleObject.put("target", rule.getTarget());
            ruleObject.put("modifier", rule.getModifier());
            ruleObject.put("value", rule.getValue());
            ruleObject.put("invert", rule.getInvert());
            ruleObject.put("operator", (rule.getOperator() == null)? "null" : rule.getOperator());
        }
        dynamicBlock.put("rulesOperator", "AND");
        dynamicBlock.put("disableTemplating", false);
        dynamicBlock.put("fallbackTo404", false);
        dynamicBlock.put("default", false);
        return dynamicBlock;
    }

    private ObjectNode createSpRecommendationExceptionBlock() {
        ObjectNode exceptionBlock = new ObjectMapper().createObjectNode();
        exceptionBlock.put("uuid", UUID.randomUUID().toString());
        exceptionBlock.put("body", "{\n    \"__type\": \"ValidationException\",\n    \"message\": \"validation error.\"\n}");
        exceptionBlock.put("latency", 0);
        exceptionBlock.put("statusCode", 400);
        exceptionBlock.put("label", "AWS SP Recommendation With Exception");
        exceptionBlock.putArray("headers");
        exceptionBlock.put("bodyType", "INLINE");
        exceptionBlock.put("filePath", "");
        exceptionBlock.put("databucketID", "");
        exceptionBlock.put("sendFileAsBody", false);

        ArrayNode rulesArray = exceptionBlock.putArray("rules");
        ObjectNode ruleObject = rulesArray.addObject();
        ruleObject.put("target", "header");
        ruleObject.put("modifier", X_AMZ_TARGET);
        ruleObject.put("value", AWS_SP_RECOMMENDATION_API_TARGET);
        ruleObject.put("invert", false);
        ruleObject.put("operator", "equals");

        exceptionBlock.put("rulesOperator", "OR");
        exceptionBlock.put("disableTemplating", false);
        exceptionBlock.put("fallbackTo404", false);
        exceptionBlock.put("default", false);
        return exceptionBlock;
    }

    private ObjectNode createUnknownOperationExceptionBlock() {
        ObjectNode exceptionBlock = new ObjectMapper().createObjectNode();
        exceptionBlock.put("uuid", UUID.randomUUID().toString());
        exceptionBlock.put("body", "{\n    \"__type\": \"UnknownOperationException\"\n}");
        exceptionBlock.put("latency", 0);
        exceptionBlock.put("statusCode", 400);
        exceptionBlock.put("label", "default");
        exceptionBlock.putArray("headers");
        exceptionBlock.put("bodyType", "INLINE");
        exceptionBlock.put("filePath", "");
        exceptionBlock.put("databucketID", "");
        exceptionBlock.put("sendFileAsBody", false);
        exceptionBlock.putArray("rules");

        exceptionBlock.put("rulesOperator", "OR");
        exceptionBlock.put("disableTemplating", false);
        exceptionBlock.put("fallbackTo404", false);
        exceptionBlock.put("default", true);
        return exceptionBlock;
    }
}
