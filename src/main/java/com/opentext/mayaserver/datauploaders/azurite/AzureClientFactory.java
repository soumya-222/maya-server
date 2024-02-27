package com.opentext.mayaserver.datauploaders.azurite;


import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.opentext.mayaserver.environments.ConfigUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.opentext.mayaserver.utility.Constants.AZURITE_POD_SUFFIX;
import static com.opentext.mayaserver.utility.Constants.HTTP;

/**
 * @author Rajiv
 */

@Slf4j
@Component
public class AzureClientFactory {

    public BlobServiceClient getBlobServiceClient(String useCaseName) {
        String serviceName = AZURITE_POD_SUFFIX + "-" + useCaseName + "-svc";
        String endpoint = HTTP + serviceName + ":10000"  + "/" + useCaseName;
        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(useCaseName, ConfigUtils.getBase64(useCaseName));
        return new BlobServiceClientBuilder()
                .endpoint(endpoint)
                .credential(credential)
                .buildClient();
    }
}
