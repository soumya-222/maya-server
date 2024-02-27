package com.opentext.mayaserver.environments.mockoon;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.opentext.mayaserver.utility.Constants;
import com.opentext.mayaserver.utility.EndPointEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.UUID;

import static com.opentext.mayaserver.utility.Constants.AWS_ORGANIZATIONS_VERSION;

@Component
@Slf4j
@RequiredArgsConstructor
public class MockoonConfigUtility {
    public JsonNode addDynamicBlockAWSAccount(JsonNode jsonNode, String filePath, String endPointType, String accountId) {
        ObjectNode dynamicBlock = createDynamicBlock(filePath, endPointType, accountId);
        ObjectNode objectNode = (ObjectNode) jsonNode;
        ArrayNode responseArray = (ArrayNode) objectNode.get("routes").get(0).get("responses");
        responseArray.add(dynamicBlock);
        return jsonNode;
    }

    private ObjectNode createDynamicBlock(String filePath, String endPointType, String accountId) {
        String nextToken = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
        ObjectNode dynamicBlock = new ObjectMapper().createObjectNode();
        dynamicBlock.put("uuid", UUID.randomUUID().toString());
        dynamicBlock.put("body", "{}");
        dynamicBlock.put("latency", 0);
        dynamicBlock.put("statusCode", 200);
        switch (endPointType) {
            case Constants.AWS_LIST_ACCOUNTS -> dynamicBlock.put("label", EndPointEnum.AWS_LIST_ACCOUNTS.value);
            case Constants.AWS_DESCRIBE_ACCOUNT -> dynamicBlock.put("label", EndPointEnum.AWS_DESCRIBE_ACCOUNT.value);
            case Constants.AWS_DESCRIBE_ORGANIZATION ->
                    dynamicBlock.put("label", EndPointEnum.AWS_DESCRIBE_ORGANIZATION.value);
        }

        ArrayNode headersArray = dynamicBlock.putArray("headers");
        ObjectNode headerObject = headersArray.addObject();
        headerObject.put("key", "X-Amz-Target");
        switch (endPointType) {
            case Constants.AWS_LIST_ACCOUNTS ->
                    headerObject.put("value", AWS_ORGANIZATIONS_VERSION + "." + EndPointEnum.AWS_LIST_ACCOUNTS.value);
            case Constants.AWS_DESCRIBE_ACCOUNT ->
                    headerObject.put("value", AWS_ORGANIZATIONS_VERSION + "." + EndPointEnum.AWS_DESCRIBE_ACCOUNT.value);
            case Constants.AWS_DESCRIBE_ORGANIZATION ->
                    headerObject.put("value", AWS_ORGANIZATIONS_VERSION + "." + EndPointEnum.AWS_DESCRIBE_ORGANIZATION.value);
        }

        dynamicBlock.put("bodyType", "FILE");
        dynamicBlock.put("filePath", filePath);
        dynamicBlock.put("databucketID", "");
        dynamicBlock.put("sendFileAsBody", true);

        ArrayNode rulesArray = dynamicBlock.putArray("rules");
        ObjectNode ruleObject = rulesArray.addObject();
        ruleObject.put("target", "body");
        ruleObject.put("modifier", "");
        if (endPointType.equals(Constants.AWS_LIST_ACCOUNTS)) {
            ruleObject.put("value", "{\"MaxResults\":${maxResults},\"NextToken\":\"" + nextToken + "\"}");
        } else if (endPointType.equals(Constants.AWS_DESCRIBE_ACCOUNT)) {
            ruleObject.put("value", "{\"AccountId\":\"" + accountId + "\"}");
        } else if (endPointType.equals(Constants.AWS_DESCRIBE_ORGANIZATION)) {
            ruleObject.put("value", "{}");
        }
        ruleObject.put("invert", false);
        ruleObject.put("operator", "equals");

        dynamicBlock.put("rulesOperator", "OR");
        dynamicBlock.put("disableTemplating", false);
        dynamicBlock.put("fallbackTo404", false);
        dynamicBlock.put("default", false);

        return dynamicBlock;
    }
}
