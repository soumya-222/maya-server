package com.opentext.mayaserver.datagenerators;

import com.opentext.mayaserver.config.ApplicationPropertiesConfig;
import com.opentext.mayaserver.datagenerators.aws.account.AWSAccountDataGenerator;
import com.opentext.mayaserver.datagenerators.aws.billing.AWSBillingDataGenerator;
import com.opentext.mayaserver.datagenerators.aws.recommendation.AWSRecommendationAsyncGenerator;
import com.opentext.mayaserver.datagenerators.azure.billing.AzureBillingDataGenerator;
import com.opentext.mayaserver.datauploaders.s3mock.S3UploadService;
import com.opentext.mayaserver.environments.azurite.AzuriteYamlCreatorImpl;
import com.opentext.mayaserver.environments.mockoon.ConfigFileCreator;
import com.opentext.mayaserver.environments.mockoon.recommendation.RecommendationConfigFileCreator;
import com.opentext.mayaserver.environments.s3mock.S3MockYamlCreatorImpl;
import com.opentext.mayaserver.models.StateEnum;
import com.opentext.mayaserver.models.vo.UseCaseVO;
import com.opentext.mayaserver.services.DataGeneratorService;
import com.opentext.mayaserver.services.UseCaseDBOperationService;
import com.opentext.mayaserver.utility.ProfileHandler;
import com.opentext.mayaserver.utility.UseCaseServiceHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.opentext.mayaserver.utility.Constants.*;

/**
 * @author Rajiv
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class RuleEngineImpl implements RuleEngine {

    private final AWSAccountDataGenerator awsAccountDataGenerator;
    private final AWSBillingDataGenerator awsBillingDataGenerator;
    private final AzureBillingDataGenerator azureBillingDataGenerator;
    private final AWSRecommendationAsyncGenerator awsRecommendationAsyncGenerator;
    @Autowired
    @Qualifier("mockoonConfigFileCreatorImpl")
    private ConfigFileCreator configFileCreator;
    @Autowired
    @Qualifier("mockoonConfigFileCreatorBYODImpl")
    private ConfigFileCreator mockoonConfigFileCreatorBYOD;
    private final RecommendationConfigFileCreator recommendationConfigFileCreator;
    private final DataGeneratorService dataGeneratorService;
    private final S3MockYamlCreatorImpl s3MockYamlCreator;
    private final AzuriteYamlCreatorImpl azuriteYamlCreator;
    private final UseCaseServiceHelper useCaseServiceHelper;
    private final S3UploadService s3UploadService;
    private final ProfileHandler profileHandler;
    private final ApplicationPropertiesConfig applicationPropertiesConfig;
    private final UseCaseDBOperationService useCaseDBOperationService;

    @Async
    public void cloudProviderDelegator(UseCaseVO useCaseVO) {
        log.info("Cloud Provider delegator invoked.");
        String useCaseName = useCaseVO.getUseCaseName();
        try {
            String cloudProvider = useCaseVO.getCloudProvider().toLowerCase();
            switch (cloudProvider) {
                case AWS -> {
                    s3MockYamlCreator.prepareYaml(useCaseName);
                    if (useCaseServiceHelper.isBYODMode(useCaseVO) && profileHandler.isProductionProfileActive()) {
                        mockoonConfigFileCreatorBYOD.prepareAccountConfigFile(useCaseName);
                        mockoonConfigFileCreatorBYOD.prepareRecommendationConfigFile(useCaseName);
                        s3UploadService.uploadAWSBillToS3(BUCKET_NAME, applicationPropertiesConfig.getByodDataPath() + FILE_PATH_SEPARATOR + AWS_CLOUD_PROVIDER + FILE_PATH_SEPARATOR + useCaseName + FILE_PATH_SEPARATOR + BILLING, useCaseName, useCaseVO.getMode());
                        useCaseDBOperationService.updateStatusOfUseCase(useCaseName, StateEnum.CREATED);
                    } else if (useCaseServiceHelper.isGenerateMode(useCaseVO)) {
                        String rootAccountID = dataGeneratorService.getRootAccount(useCaseName);
                        List<String> memberAccountIDs = dataGeneratorService.getMemberAccounts(useCaseName);
                        configFileCreator.prepareAccountConfigFile(useCaseName);
                        recommendationConfigFileCreator.prepareConfigFile(useCaseName);
                        awsAccountDataGenerator.generateAccounts(useCaseVO);
                        awsBillingDataGenerator.generateBills(useCaseVO, rootAccountID, memberAccountIDs);
                        awsRecommendationAsyncGenerator.generateRecommendations(useCaseVO);
                    }
                }
                case AZURE -> {
                    azuriteYamlCreator.prepareYaml(useCaseVO.getUseCaseName());
                    if (useCaseServiceHelper.isBYODMode(useCaseVO)) {
                        mockoonConfigFileCreatorBYOD.prepareAccountConfigFile(useCaseName);
                        mockoonConfigFileCreatorBYOD.prepareRecommendationConfigFile(useCaseName);
                    } else if (useCaseServiceHelper.isGenerateMode(useCaseVO)) {
                        azureBillingDataGenerator.generateAzureBills(useCaseVO);
                    }

                }
            }
        } catch (Exception e) {
            log.error("Error reported while usecase '{}' execution, Caused By: {}", useCaseName, e.getMessage());
            useCaseDBOperationService.updateStatusOfUseCase(useCaseName, StateEnum.FAILED);
        }
    }

}
