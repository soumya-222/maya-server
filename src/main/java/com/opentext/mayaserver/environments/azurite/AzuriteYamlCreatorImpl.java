package com.opentext.mayaserver.environments.azurite;

import com.opentext.mayaserver.config.ApplicationPropertiesConfig;
import com.opentext.mayaserver.datagenerators.aws.recommendation.RecommendationHelper;
import com.opentext.mayaserver.environments.K8sObjectsManager;
import com.opentext.mayaserver.environments.YamlFileCreator;
import com.opentext.mayaserver.exceptions.MayaServerException;
import com.opentext.mayaserver.models.UseCase;
import com.opentext.mayaserver.repository.UseCaseRepository;
import com.opentext.mayaserver.services.UseCaseAPIService;
import com.opentext.mayaserver.utility.CommonUtils;
import com.opentext.mayaserver.utility.ProfileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AzuriteYamlCreatorImpl implements YamlFileCreator {

    private final UseCaseRepository useCaseRepository;
    private final ApplicationPropertiesConfig applicationPropertiesConfig;
    private final K8sObjectsManager K8sObjectsManager;
    private final RecommendationHelper recommendationHelper;
    private UseCaseAPIService useCaseAPIService;
    private final ProfileHandler profileHandler;

    @Autowired
    public void setUseCaseAPIService(@Lazy UseCaseAPIService useCaseAPIService) {
        this.useCaseAPIService = useCaseAPIService;
    }


    @Override
    public void prepareYaml(String useCaseName) {
        log.debug("Preparing azurite yaml file, current status {}", useCaseAPIService.getUseCaseByNameOrId(useCaseName).getState().name());
        if (profileHandler.isProductionProfileActive()) {
            try {
                String configFilePath = CommonUtils.createDirectoryBasedOnUseCase(useCaseName, applicationPropertiesConfig.getAzurite().getNfs());
                UseCase useCase = useCaseRepository.findByUseCaseName(useCaseName).get();
                K8sObjectsManager.prepareAzuriteYaml(configFilePath, useCase);
                log.debug("Azurite Yaml File Updated Successfully");

            } catch (Exception e) {
                log.error("Unable to create azurite yaml file {}", useCaseName);
                throw new MayaServerException(e.getMessage());
            }
        }
    }
}
