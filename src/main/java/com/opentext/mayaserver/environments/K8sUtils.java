package com.opentext.mayaserver.environments;

import com.opentext.mayaserver.exceptions.MayaServerException;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.apis.NetworkingV1Api;
import io.kubernetes.client.openapi.auth.ApiKeyAuth;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.opentext.mayaserver.utility.Constants.BEARER_TOKEN_FILE_PATH;
import static com.opentext.mayaserver.utility.Constants.NAMESPACE_PATH;
import static java.nio.file.Files.readAllBytes;

/**
 * @author Soumyaranjan
 */

public class K8sUtils {

    public static V1Deployment createDeployment(V1Deployment deployment, String namespace) throws ApiException {
        AppsV1Api api = createApi(AppsV1Api.class);
        V1Deployment result = api.createNamespacedDeployment(namespace, deployment, null, null, null, null);
        return result;
    }

    public static V1Status deleteDeployments(List<String> deploymentNames, String namespace) throws ApiException {
        AppsV1Api api = createApi(AppsV1Api.class);
        V1DeleteOptions deleteOptions = new V1DeleteOptions();
        V1Status result = null;
        for (String deploymentName : deploymentNames) {
            result = api.deleteNamespacedDeployment(deploymentName, namespace, null, null, null, null, null, deleteOptions);
        }
        return result;
    }

    public static V1Service createService(V1Service service, String namespace) throws ApiException {
        CoreV1Api serviceApi = createApi(CoreV1Api.class);
        V1Service result = serviceApi.createNamespacedService(namespace, service, null, null, null, null);
        return result;
    }

    public static void deleteServices(List<String> serviceNames, String namespace) throws ApiException {
        CoreV1Api serviceApi = createApi(CoreV1Api.class);
        V1DeleteOptions deleteOptions = new V1DeleteOptions();
        for (String serviceName:serviceNames) {
            serviceApi.deleteNamespacedService(serviceName, namespace, null, null, null, null, null, deleteOptions);
        }
    }

    public static V1Ingress createIngress(V1Ingress ingress, String namespace) throws ApiException {
        NetworkingV1Api ingressApi = createApi(NetworkingV1Api.class);
        V1Ingress result = ingressApi.createNamespacedIngress(namespace, ingress, null, null, null, null);
        return result;
    }

    public static void deleteIngresses(List<String> ingressNames, String namespace) throws ApiException {
        NetworkingV1Api api = createApi(NetworkingV1Api.class);
        V1DeleteOptions deleteOptions = new V1DeleteOptions();
        for (String ingressName:ingressNames) {
            api.deleteNamespacedIngress(ingressName, namespace, null, null, null, null, null, deleteOptions);
        }
    }

    private static ApiClient createApiClient() {
        try {
            ApiClient apiClient = Config.defaultClient();
            Configuration.setDefaultApiClient(apiClient);

            // Configure API key authorization: BearerToken
            ApiKeyAuth bearerToken = (ApiKeyAuth) apiClient.getAuthentication("BearerToken");
            String bearerTokenValue = new String(readAllBytes(Paths.get(BEARER_TOKEN_FILE_PATH)));
            bearerToken.setApiKey(bearerTokenValue);

            return apiClient;
        } catch (IOException e) {
            throw new MayaServerException("Could not configure K8s Api Client: ", e);
        }
    }

    public static <T> T createApi(Class<T> type) {
        ApiClient apiClient = createApiClient();
        if (type.equals(AppsV1Api.class)) {
            return type.cast(new AppsV1Api(apiClient));
        } else if (type.equals(CoreV1Api.class)) {
            return type.cast(new CoreV1Api(apiClient));
        } else if (type.equals(NetworkingV1Api.class)) {
            return type.cast(new NetworkingV1Api(apiClient));
        } else {
            throw new MayaServerException("Unsupported API type");
        }
    }

    public static String getNamespace() throws IOException {
        return new String(readAllBytes(Paths.get(NAMESPACE_PATH)));
    }

    public static V1DeploymentList listDeployments(String namespace) throws ApiException {
        AppsV1Api api = createApi(AppsV1Api.class);
        V1DeploymentList deploymentList = api.listNamespacedDeployment(namespace, null, null, null, null, null, null, null, null, null, null);
        return deploymentList;
    }

    public static V1ServiceList listServices(String namespace) throws ApiException {
        CoreV1Api serviceApi = createApi(CoreV1Api.class);
        V1ServiceList serviceList = serviceApi.listNamespacedService(namespace, null, null, null, null, null, null, null, null, null, null);
        return serviceList;
    }

    public static V1IngressList listIngress(String namespace) throws ApiException {
        NetworkingV1Api ingressApi = createApi(NetworkingV1Api.class);
        V1IngressList ingressList = ingressApi.listNamespacedIngress(namespace, null, null, null, null, null, null, null, null, null, null);
        return ingressList;
    }

    public static List<String> getDeployments(String namespace, String usecaseName) throws ApiException {
        List<String> deploymentNames = new ArrayList<>();
        V1DeploymentList deploymentList = listDeployments(namespace);
        List<V1Deployment> deployments = deploymentList.getItems();
        String deploymentName = null;
        for (V1Deployment deployment : deployments) {
            deploymentName = deployment.getMetadata().getName();
            if (deploymentName.contains(usecaseName)) {
                deploymentNames.add(deploymentName);
            }
        }
        return deploymentNames;
    }

    public static List<String> getServices(String namespace, String usecaseName) throws ApiException {
        List<String> serviceNames = new ArrayList<>();
        V1ServiceList serviceList = listServices(namespace);
        List<V1Service> services = serviceList.getItems();
        String serviceName = null;
        for (V1Service service : services) {
            serviceName = service.getMetadata().getName();
            if (serviceName.contains(usecaseName)) {
                serviceNames.add(serviceName);
            }
        }
        return serviceNames;
    }

    public static List<String> getIngresses(String namespace, String usecaseName) throws ApiException {
        List<String> ingressNames = new ArrayList<>();
        V1IngressList ingressList = listIngress(namespace);
        List<V1Ingress> ingresses = ingressList.getItems();
        String ingressName = null;
        for (V1Ingress ingress : ingresses) {
            ingressName = ingress.getMetadata().getName();
            if (ingressName.contains(usecaseName)) {
                ingressNames.add(ingressName);
            }
        }
        return ingressNames;
    }
}
