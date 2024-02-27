package com.opentext.mayaserver.environments;

import com.opentext.mayaserver.environments.K8sUtils;
import com.opentext.mayaserver.exceptions.MayaServerException;
import com.opentext.mayaserver.models.UseCase;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1Ingress;
import io.kubernetes.client.openapi.models.V1Service;
import io.kubernetes.client.util.Yaml;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.opentext.mayaserver.utility.Constants.*;

/**
 * @author Soumyaranjan
 */

@Component
@Slf4j
public class K8sObjectsManager {
    public void prepareYaml(String configFilePath,UseCase useCase) throws IOException {
        String namespace = K8sUtils.getNamespace();
        try {
            String deploymentYamlContent = readFileFromResource(DEPLOYMENT_YAML_TEMPLATE);
            String serviceYamlContent = readFileFromResource(SERVICE_YAML_TEMPLATE);
            String ingressYamlContent = readFileFromResource(INGRESS_YAML_TEMPLATE);
            Map<String, String> placeholderValues = createPlaceholderValues(useCase,namespace,"maya-account");
            String updatedDeploymentYAML = replacePlaceholders(deploymentYamlContent, placeholderValues);
            String updatedServiceYAML = replacePlaceholders(serviceYamlContent, placeholderValues);
            String updatedIngressYAML = replacePlaceholders(ingressYamlContent, placeholderValues);
            String yamlFile = YAML_FILE_PREFIX + "-" + useCase.getUseCaseName() + YAML;
            String fileName = FILE_PATH_SEPARATOR + yamlFile;
            String updatedFilePath = configFilePath + fileName;
            writeYAMLFile(updatedDeploymentYAML, updatedFilePath);
            log.info("YAML placeholder replacement completed.");
            //Creating mockoon instaces(Deployment/Service/Ingress)
            createK8sObjects(updatedDeploymentYAML,updatedServiceYAML,updatedIngressYAML,namespace,useCase);
            } catch (Exception e) {
            throw new MayaServerException("Failed to prepare yaml" + useCase.getUseCaseName());
        }
    }


    public void prepareRecommendationYaml(String configFilePath,UseCase useCase) throws IOException{
        String namespace = K8sUtils.getNamespace();
        try {
            String deploymentYamlContent = readFileFromResource(RECOMMENDATION_DEPLOYMENT_YAML_TEMPLATE);
            String serviceYamlContent = readFileFromResource(RECOMMENDATION_SERVICE_YAML_TEMPLATE);
            String ingressYamlContent = readFileFromResource(RECOMMENDATION_INGRESS_YAML_TEMPLATE);
            Map<String, String> placeholderValues = createPlaceholderValues(useCase, namespace, "maya-server-recommendation");
            String updatedDeploymentYAML = replacePlaceholders(deploymentYamlContent, placeholderValues);
            String updatedServiceYAML = replacePlaceholders(serviceYamlContent, placeholderValues);
            String updatedIngressYAML = replacePlaceholders(ingressYamlContent, placeholderValues);
            String yamlFile = RECOMMENDATION_YAML_FILE_PREFIX + "-" + useCase.getUseCaseName() + YAML;
            String fileName = FILE_PATH_SEPARATOR + yamlFile;
            String updatedFilePath = configFilePath + fileName;
            writeYAMLFile(updatedDeploymentYAML, updatedFilePath);
            log.info("Recommendation YAML placeholder replacement completed.");
            createK8sObjects(updatedDeploymentYAML,updatedServiceYAML,updatedIngressYAML,namespace,useCase);
        } catch (Exception e) {
            throw new MayaServerException("Failed to prepare recommendation yaml" + useCase.getUseCaseName());
        }
    }

    public void prepareAzuriteYaml(String configFilePath,UseCase useCase) throws IOException {
        String namespace = K8sUtils.getNamespace();
        try {
            String deploymentYamlContent = readFileFromResource(AZURITE_DEPLOYMENT_YAML_TEMPLATE);
            String serviceYamlContent = readFileFromResource(AZURITE_SERVICE_YAML_TEMPLATE);
            String ingressYamlContent = readFileFromResource(AZURITE_INGRESS_YAML_TEMPLATE);
            Map<String, String> placeholderValues = createPlaceholderValues(useCase, namespace, AZURITE_POD_SUFFIX);
            String updatedDeploymentYAML = replacePlaceholders(deploymentYamlContent, placeholderValues);
            String updatedServiceYAML = replacePlaceholders(serviceYamlContent, placeholderValues);
            String updatedIngressYAML = replacePlaceholders(ingressYamlContent, placeholderValues);
            String yamlFile = AZURITE_POD_SUFFIX + "-" + useCase.getUseCaseName() + YAML;
            String fileName = FILE_PATH_SEPARATOR + yamlFile;
            String updatedFilePath = configFilePath + fileName;
            writeYAMLFile(updatedDeploymentYAML, updatedFilePath);
            log.info("Azurite YAML placeholder replacement completed.");
            createK8sObjects(updatedDeploymentYAML, updatedServiceYAML, updatedIngressYAML, namespace, useCase);
        } catch (Exception e) {
            throw new MayaServerException("Failed to prepare azurite yaml" + useCase.getUseCaseName());
        }
    }

    public void prepareS3MockYaml(String configFilePath,UseCase useCase) throws IOException{
        String namespace = K8sUtils.getNamespace();
        try {
            String deploymentYamlContent = readFileFromResource(S3MOCK_DEPLOYMENT_YAML_TEMPLATE);
            String serviceYamlContent = readFileFromResource(S3MOCK_SERVICE_YAML_TEMPLATE);
            String ingressYamlContent = readFileFromResource(S3MOCK_INGRESS_YAML_TEMPLATE);
            Map<String, String> placeholderValues = createPlaceholderValues(useCase, namespace, S3MOCK_POD_SUFFIX);
            String updatedDeploymentYAML = replacePlaceholders(deploymentYamlContent, placeholderValues);
            String updatedServiceYAML = replacePlaceholders(serviceYamlContent, placeholderValues);
            String updatedIngressYAML = replacePlaceholders(ingressYamlContent, placeholderValues);
            String yamlFile = S3MOCK_YAML_FILE_PREFIX + "-" + useCase.getUseCaseName() + YAML;
            String fileName = FILE_PATH_SEPARATOR + yamlFile;
            String updatedFilePath = configFilePath + fileName;
            writeYAMLFile(updatedDeploymentYAML, updatedFilePath);
            log.info("s3mock yaml placeholder replacement completed.");
            createK8sObjects(updatedDeploymentYAML,updatedServiceYAML,updatedIngressYAML,namespace,useCase);
        } catch (Exception e) {
            throw new MayaServerException("Failed to prepare s3mock yaml" + useCase.getUseCaseName());
        }
    }

    private  Map<String, String> createPlaceholderValues(UseCase useCase, String namespace, String POD_SUFFIX) {
        Map<String, String> placeholderValues = new HashMap<>();
        placeholderValues.put("namespace", namespace);
        placeholderValues.put("usecase-name", useCase.getUseCaseName());
        String serverPort = Integer.toString(useCase.getMockoonPort());
        if (useCase.getCloudProvider().equals(AZURE)) {
            placeholderValues.put("custom-account", useCase.getUseCaseName() + ":" + ConfigUtils.getBase64(useCase.getUseCaseName()));
        }
        placeholderValues.put("server-port", serverPort);
//        String POD_SUFFIX = "maya-server-mockoon";
        placeholderValues.put("pod-name", POD_SUFFIX + "-" + useCase.getUseCaseName());
        return placeholderValues;
    }

    private  String replacePlaceholders(String yamlContent, Map<String, String> placeholderValues) {
        for (Map.Entry<String, String> entry : placeholderValues.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            String value = entry.getValue();
            yamlContent = yamlContent.replace(placeholder, value);
        }
        return yamlContent;
    }

    private void writeYAMLFile(String yamlContent, String filePath) throws IOException {
        Files.writeString(Path.of(filePath), yamlContent, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public void createK8sObjects(String deploymentYamlContent, String serviceYamlContent, String ingressYamlContent, String namespace, UseCase useCase){
        try {
            V1Deployment deployment = Yaml.loadAs(deploymentYamlContent, V1Deployment.class);
            V1Deployment deploymentResult = K8sUtils.createDeployment(deployment, namespace);
            log.info("Deployment created successfully '{}'", deploymentResult.getMetadata().getName());
            V1Service service = Yaml.loadAs(serviceYamlContent, V1Service.class);
            V1Service serviceResult = K8sUtils.createService(service, namespace);
            log.info("Service created successfully '{}'", serviceResult.getMetadata().getName());
            V1Ingress ingress = Yaml.loadAs(ingressYamlContent, V1Ingress.class);
            V1Ingress ingressResult = K8sUtils.createIngress(ingress, namespace);
            log.info("Ingress created successfully '{}'" , ingressResult.getMetadata().getName());
        } catch (ApiException e) {
            log.error("Exception when calling NetworkingV1Api#createNamespacedIngress\n"
                    + "Status code: " + e.getCode() + "\n"
                    + "Reason: " + e.getResponseBody() + "\n"
                    + "Response headers: " + e.getResponseHeaders() + "\n"
                    + "Error creating mockoon container: " + e.getMessage());
            throw new MayaServerException("Failed to create Mockoon instance " + useCase.getUseCaseName());
        } catch (Exception exception){
            log.error("Exception when calling NetworkingV1Api#createNamespacedIngress\n"
                    + "Error creating mockoon container '{}'", exception.getMessage());
            throw new MayaServerException("Failed to create Mockoon instance" , exception);
        }
    }
    public void deleteK8Objects(String namespace,String useCaseName){
        try {
            List<String> deploymentNames = K8sUtils.getDeployments(namespace, useCaseName);
            K8sUtils.deleteDeployments(deploymentNames, namespace);
            List<String> serviceNames = K8sUtils.getServices(namespace, useCaseName);
            K8sUtils.deleteServices(serviceNames,namespace);
            List<String> ingressNames = K8sUtils.getIngresses(namespace, useCaseName);
            K8sUtils.deleteIngresses(ingressNames,namespace);
        } catch (ApiException e) {
            log.error("Exception when calling AppsV1Api#deleteNamespacedDeployment\n"
                    + "Status code: {} \n"
                    + "Reason: {} \n"
                    + "Response headers: {} \n"
                    + "Error creating mockoon container: " + e.getMessage(), e.getCode(), e.getResponseBody(), e.getResponseHeaders());
            throw new MayaServerException("Failed to Delete Mockoon instance " + useCaseName);
        } catch (Exception exception){
            log.error("Exception when calling NetworkingV1Api#createNamespacedIngress\n"
                    + "Error creating mockoon container '{}'", exception.getMessage());
            throw new MayaServerException("Failed to create Mockoon instance" , exception);
        }
    }

    public String readFileFromResource(String filePath) throws IOException {
        Resource resource = new ClassPathResource("/yaml-templates/" + filePath);
        InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        return FileCopyUtils.copyToString(reader);
    }
}
