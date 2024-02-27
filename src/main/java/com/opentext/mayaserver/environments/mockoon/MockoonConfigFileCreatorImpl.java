package com.opentext.mayaserver.environments.mockoon;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentext.mayaserver.config.ApplicationPropertiesConfig;
import com.opentext.mayaserver.environments.ConfigUtils;
import com.opentext.mayaserver.exceptions.MayaServerException;
import com.opentext.mayaserver.models.StateEnum;
import com.opentext.mayaserver.models.UseCase;
import com.opentext.mayaserver.models.vo.AccountMetadataVO;
import com.opentext.mayaserver.repository.UseCaseRepository;
import com.opentext.mayaserver.services.DataGeneratorService;
import com.opentext.mayaserver.services.impl.UseCaseDBOperationServiceImpl;
import com.opentext.mayaserver.utility.CommonUtils;
import com.opentext.mayaserver.utility.ProfileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

import static com.opentext.mayaserver.utility.Constants.*;

/**
 * @author Soumyaranjan
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class MockoonConfigFileCreatorImpl implements ConfigFileCreator {

    private final UseCaseRepository useCaseRepository;
    private final DataGeneratorService dataGeneratorService;
    private final ApplicationPropertiesConfig applicationPropertiesConfig;
    private final com.opentext.mayaserver.environments.K8sObjectsManager K8sObjectsManager;
    private final ProfileHandler profileHandler;
    private final UseCaseDBOperationServiceImpl useCaseDBOperationService;

    @Override
    public void prepareAccountConfigFile(String useCaseName) {
        log.info("Generating mockoon config file and updating status to {}", StateEnum.IN_PROGRESS);
        useCaseDBOperationService.updateStatusOfUseCase(useCaseName, StateEnum.IN_PROGRESS);
        if (profileHandler.isProductionProfileActive()) {
            try {
                List<AccountMetadataVO> accountMetadataVOList = getFilenamesFromDatabase(useCaseName);
                String configFilePath = CommonUtils.createDirectoryBasedOnUseCase(useCaseName, applicationPropertiesConfig.getMockoon().getNfsConfigPath());
                String fileContent = CommonUtils.readFileFromResource(MOCKOON_TEMPLATE_FILE_PATH, TEMPLATE_FILE_NAME + JSON);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(fileContent);
                UseCase useCase = useCaseRepository.findByUseCaseName(useCaseName).get();
                String accountID = dataGeneratorService.getRootAccount(useCaseName);
                MockoonConfigUtility mockoonConfigUtility = new MockoonConfigUtility();
                for (AccountMetadataVO accountMetadataVO : accountMetadataVOList) {
                    if (accountMetadataVO != null) {
                        String endPointType = accountMetadataVO.getEndpointType();
                        List<String> filePaths = accountMetadataVO.getDataFilePath();
                        for (String filePath : filePaths) {
                            JsonNode updatedJsonNode = mockoonConfigUtility.addDynamicBlockAWSAccount(jsonNode, filePath, endPointType, accountID);
                            if (updatedJsonNode != null) {
                                jsonNode = updatedJsonNode;
                            }
                        }
                        if (jsonNode != null) {
                            ConfigUtils.traverseAndUpdate(jsonNode, useCase);
                        }
                    }
                }
                File updatedFile = new File(configFilePath + FILE_PATH_SEPARATOR+ TEMPLATE_FILE_NAME + "_" + useCaseName + JSON);
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(updatedFile, jsonNode);
                log.info("Mockoon Configfile Created Successfully");
                K8sObjectsManager.prepareYaml(configFilePath, useCase);
                log.info("Mockoon Yaml File Updated Successfully");

            } catch (Exception e) {
                log.error("Unable to create mockoon configuration file {}", useCaseName);
                throw new MayaServerException(e.getMessage());
            }
        }
    }

    private List<AccountMetadataVO> getFilenamesFromDatabase(String useCaseName) {
        return dataGeneratorService.getAccountMetaData(useCaseName);
    }

    @Override
    public void prepareRecommendationConfigFile(String useCaseName) {
        // TODO: Recommendation mockoon config file preparation
    }
}
