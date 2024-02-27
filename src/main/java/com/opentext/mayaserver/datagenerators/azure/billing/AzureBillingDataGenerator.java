package com.opentext.mayaserver.datagenerators.azure.billing;

import com.opentext.mayaserver.config.ApplicationPropertiesConfig;
import com.opentext.mayaserver.datauploaders.azurite.AzuriteBlobUploadService;
import com.opentext.mayaserver.models.StateEnum;
import com.opentext.mayaserver.models.vo.UseCaseVO;
import com.opentext.mayaserver.services.UseCaseAPIService;
import com.opentext.mayaserver.utility.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static com.opentext.mayaserver.utility.Constants.*;

/**
 * @author Rajiv
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class AzureBillingDataGenerator {

    private UseCaseAPIService useCaseAPIService;
    private final ApplicationPropertiesConfig applicationPropertiesConfig;
    private final AzuriteBlobUploadService azuriteBlobUploadService;

    @Autowired
    public void setUseCaseAPIService(@Lazy UseCaseAPIService useCaseAPIService) {
        this.useCaseAPIService = useCaseAPIService;
    }

    @Async
    public void generateAzureBills(UseCaseVO useCaseVO) {
        uploadAzureBill(useCaseVO);
    }

    private void uploadAzureBill(UseCaseVO useCaseVO) {
        InputStream inputStream = null;
        InputStream manifestInputStream = null;
        try {
            String absolutePath = CommonUtils.createDirectoryBasedOnUseCase(useCaseVO.getUseCaseName(), applicationPropertiesConfig.getAzurite().getNfs());
            inputStream = this.getClass().getResourceAsStream(LOCAL_AZURE_FILE_PATH);
            //todo: this is sample data only this needs to remove once actual generation logic is built
            manifestInputStream = this.getClass().getResourceAsStream("/azure-bill-template/" + "_manifest.json");
            Files.copy(inputStream, Paths.get(absolutePath + FILE_PATH_SEPARATOR + AZURE_TEMPLATE_FILE_NAME), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(manifestInputStream, Paths.get(absolutePath + FILE_PATH_SEPARATOR + "_manifest.json"), StandardCopyOption.REPLACE_EXISTING);
            log.info("uploading bill to azurite blob storage ...");
            azuriteBlobUploadService.uploadAzureBillToBlob(useCaseVO.getUseCaseName());
            useCaseAPIService.updateStatusOfUseCase(useCaseVO.getUseCaseName(), StateEnum.CREATED);
        } catch (IOException e) {
            useCaseAPIService.updateStatusOfUseCase(useCaseVO.getUseCaseName(), StateEnum.FAILED);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Error reported while uploading to NFS {}", applicationPropertiesConfig.getAzurite().getNfs());
                }
            }
        }
    }
}
