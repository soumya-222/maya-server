package com.opentext.mayaserver.environments;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.opentext.mayaserver.models.UseCase;
import com.opentext.mayaserver.utility.DefaultData;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.opentext.mayaserver.utility.Constants.FILE_PATH_SEPARATOR;
import static com.opentext.mayaserver.utility.Constants.MANIFEST_FILE_NAME;

@Slf4j
public class ConfigUtils {
    private static String accessKey = "Dummy";
    private static String secretKey = "DummyKey";

    public static void traverseAndUpdate(JsonNode node, UseCase useCase) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                JsonNode value = entry.getValue();
                if (value.isTextual()) {
                    String textValue = value.asText();
                    if (textValue.contains("${")) {
                        String updatedValue = updatePlaceholders(textValue, useCase);
                        objectNode.put(entry.getKey(), updatedValue);
                    }
                } else {
                    traverseAndUpdate(value, useCase);
                }
            }
        } else if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            for (JsonNode element : arrayNode) {
                traverseAndUpdate(element, useCase);
            }
        }
    }

    private static String updatePlaceholders(String value, UseCase useCase) {
        String updatedText = value;
        Matcher matcher = Pattern.compile("\\$\\{(.*?)\\}").matcher(value);
        while (matcher.find()) {
            String placeholder = matcher.group(1);
            String updatedValue = getUpdatedValueForPlaceholder(placeholder, useCase);
            updatedText = updatedText.replace("${" + placeholder + "}", updatedValue);
        }
        return updatedText;
    }

    private static String getUpdatedValueForPlaceholder(String placeholder, UseCase useCase) {
        switch (placeholder) {
            case "usecase-name" -> {
                return useCase.getUseCaseName();
            }
            case "maxResults" -> {
                return String.valueOf(DefaultData.PAGE_SIZE);
            }
            case "server-port" -> {
                return Integer.toString(useCase.getMockoonPort());
            }
        }
        return "${" + placeholder + "}";
    }

    public static String getBase64(String accountName){
        return Base64.getEncoder().encodeToString(accountName.getBytes());
    }

    public static List<String> readBlobNames(File parentFolder) {
        List<String> blobNames = new ArrayList<>();
        if (!parentFolder.isDirectory()) {
            throw new IllegalArgumentException("The provided path is not a directory.");
        }
        processFolder(parentFolder, blobNames);
        return blobNames;
    }
    private static void processFolder(File folder, List<String> blobNames) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    processFolder(file, blobNames);
                } else if (file.getName().toLowerCase().endsWith(".json")) {
                    blobNames.addAll(parseManifestFile(file));
                }
            }
        }
    }

    private static List<String> parseManifestFile(File file) {
        List<String> blobNames = new ArrayList<>();
        String blobName = null;
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(file));
            JSONObject manifest = (JSONObject) obj;
            JSONArray blobs = (JSONArray) manifest.get("blobs");

            for (Object blobObj : blobs) {
                JSONObject blob = (JSONObject) blobObj;
                blobName = (String) blob.get("blobName");
                blobNames.add(blobName);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        if(blobName != null) {
            String manifestBlobName = blobName.substring(0, blobName.lastIndexOf("/")) + "/" + MANIFEST_FILE_NAME;
            blobNames.add(manifestBlobName);
        }
        return blobNames;
    }

    private static String getFileName(String blobName) {
        return blobName.substring(blobName.lastIndexOf(FILE_PATH_SEPARATOR) + 1);
    }
}
