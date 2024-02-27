package com.opentext.mayaserver.environments.mockoon;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentext.mayaserver.byod.aws.AccountsUtility;
import com.opentext.mayaserver.config.ApplicationPropertiesConfig;
import com.opentext.mayaserver.environments.ConfigUtils;
import com.opentext.mayaserver.exceptions.MayaServerException;
import com.opentext.mayaserver.models.AwsAccountDetails;
import com.opentext.mayaserver.models.StateEnum;
import com.opentext.mayaserver.models.UseCase;
import com.opentext.mayaserver.models.vo.AccountMetadataVO;
import com.opentext.mayaserver.repository.UseCaseRepository;
import com.opentext.mayaserver.services.UseCaseDBOperationService;
import com.opentext.mayaserver.utility.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

import static com.opentext.mayaserver.utility.Constants.*;

@RequiredArgsConstructor
@Slf4j
@Component
public class MockoonConfigFileCreatorBYODImpl implements ConfigFileCreator {

    private final UseCaseRepository useCaseRepository;
    private final ApplicationPropertiesConfig applicationPropertiesConfig;
    private final com.opentext.mayaserver.environments.K8sObjectsManager K8sObjectsManager;
    private final AccountsUtility accountsUtility;
    private final UseCaseDBOperationService useCaseDBOperationService;

    @Override
    public void prepareAccountConfigFile(String useCaseName) {
        log.info("Generating mockoon config file for BYOD accounts and updating status to {}", StateEnum.IN_PROGRESS);
        useCaseDBOperationService.updateStatusOfUseCase(useCaseName, StateEnum.IN_PROGRESS);
        try {
            AwsAccountDetails awsAccountDetails = accountsUtility.getAwsAccountDetails(useCaseName);
            if (awsAccountDetails != null) {
                List<AccountMetadataVO> accountMetadataVOList = awsAccountDetails.getAccountMetadataVOList();
                String rootAccountID = awsAccountDetails.getRootAccountID();
                String configFilePath = CommonUtils.createDirectoryBasedOnUseCase(useCaseName, applicationPropertiesConfig.getMockoon().getNfsConfigPath());
                String fileContent = CommonUtils.readFileFromResource(MOCKOON_TEMPLATE_FILE_PATH, TEMPLATE_FILE_NAME + JSON);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(fileContent);
                UseCase useCase = useCaseRepository.findByUseCaseName(useCaseName).get();
                MockoonConfigUtility mockoonConfigUtility = new MockoonConfigUtility();
                for (AccountMetadataVO accountMetadataVO : accountMetadataVOList) {
                    if (accountMetadataVO != null) {
                        String endPointType = accountMetadataVO.getEndpointType();
                        List<String> filePaths = accountMetadataVO.getDataFilePath();
                        for (String filePath : filePaths) {
                            JsonNode updatedJsonNode = mockoonConfigUtility.addDynamicBlockAWSAccount(jsonNode, filePath, endPointType, rootAccountID);
                            if (updatedJsonNode != null) {
                                jsonNode = updatedJsonNode;
                            }
                        }
                        if (jsonNode != null) {
                            ConfigUtils.traverseAndUpdate(jsonNode, useCase);
                        }
                    }
                }
                File updatedFile = new File(configFilePath + File.separator + TEMPLATE_FILE_NAME + "_" + useCaseName + JSON);
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(updatedFile, jsonNode);
                log.info("Mockoon Configfile Created for BYOD data Successfully");
                K8sObjectsManager.prepareYaml(configFilePath, useCase);
                log.info("Mockoon Yaml File Updated for BYOD data Successfully");
            }
        } catch (Exception e) {
            log.error("Unable to create mockoon configuration file for BYOD data {}", useCaseName);
            throw new MayaServerException(e.getMessage());
        }

    }

    @Override
    public void prepareRecommendationConfigFile(String useCaseName) {
        // TODO: Recommendation mockoon config file preparation
    }
}
