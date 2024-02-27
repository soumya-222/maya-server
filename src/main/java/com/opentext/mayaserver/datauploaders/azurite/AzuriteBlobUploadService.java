package com.opentext.mayaserver.datauploaders.azurite;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.opentext.mayaserver.config.ApplicationPropertiesConfig;
import com.opentext.mayaserver.environments.ConfigUtils;
import com.opentext.mayaserver.exceptions.MayaServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;


import static com.opentext.mayaserver.utility.Constants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AzuriteBlobUploadService {
    private final ApplicationPropertiesConfig applicationPropertiesConfig;
    private final AzureClientFactory azureClientFactory;

    private void uploadFilesRecursively(File filePath,BlobContainerClient blobContainerClient) {
        File[] files = filePath.listFiles();
        if (files == null || files.length == 0) {
            log.error("No files found in the directory.");
            throw new MayaServerException("directory is empty");
        }

        if (blobContainerClient.exists()) {
            BlobClient blobClient;
            List blobList ;
            for (File uploadFile : files) {
                log.info("FileName" + uploadFile.getName());
                try {
                    blobList = ConfigUtils.readBlobNames(uploadFile);
                } catch (Exception e) {
                    log.error("Error while parsing manifeast file {} ", e.getMessage());
                    throw new MayaServerException(e.getMessage());
                }
                //log.info("BlobName" + blobs.get(uploadFile.getName()));
                if (uploadFile.isFile()) {
                    try {
                        blobClient = blobContainerClient.getBlobClient("");
                        upload(blobClient, uploadFile);
                    } catch (Exception e) {
                        log.error("Error while uploading bills to azurite {} Error Cause: {}", uploadFile.getName(), e.getMessage());
                        throw new MayaServerException(e.getMessage());
                    }
                } else if (uploadFile.isDirectory()) {
                    uploadFilesRecursively(uploadFile, blobContainerClient);
                }
            }
        } else {
            throw new MayaServerException("Container Not Exist" + AZURITE_CONTAINER_NAME);
        }
    }
    public void uploadAzureBillToBlob(String useCaseName) {
        BlobServiceClient blobServiceClient = azureClientFactory.getBlobServiceClient(useCaseName);
        blobServiceClient.createBlobContainerIfNotExists(AZURITE_CONTAINER_NAME);
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(AZURITE_CONTAINER_NAME);
        File filePath = new File(applicationPropertiesConfig.getAzurite().getNfs() + FILE_PATH_SEPARATOR + useCaseName);
        blobContainerClient.createIfNotExists();
        uploadFilesRecursively(filePath,blobContainerClient);
    }

    private void upload(BlobClient blobClient, File file){
        try {
            InputStream uploadFile = new FileInputStream(file);
            blobClient.upload(uploadFile);
            log.info(file.getName() + " file uploaded successfully!");
        } catch (Exception e) {
            log.error("Error uploading file: " + e.getMessage());
            throw new MayaServerException("Error uploading file:"+ file.getName());
        }
    }
}
