package com.opentext.mayaserver.environments.s3mock;

import com.opentext.mayaserver.config.ApplicationPropertiesConfig;
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
@RequiredArgsConstructor
@Slf4j
public class S3MockYamlCreatorImpl implements YamlFileCreator {
    private final UseCaseRepository useCaseRepository;
    private final ApplicationPropertiesConfig applicationPropertiesConfig;
    private final K8sObjectsManager K8sObjectsManager;
    private UseCaseAPIService useCaseAPIService;
    private final ProfileHandler profileHandler;

    @Autowired
    public void setUseCaseAPIService(@Lazy UseCaseAPIService useCaseAPIService) {
        this.useCaseAPIService = useCaseAPIService;
    }


    @Override
    public void prepareYaml(String useCaseName) {
        log.debug("Preparing s3mock yaml file, current status {}", useCaseAPIService.getUseCaseByNameOrId(useCaseName).getState().name());
        if (profileHandler.isProductionProfileActive()) {
            try {
                String configFilePath = CommonUtils.createDirectoryBasedOnUseCase(useCaseName, applicationPropertiesConfig.getAzurite().getNfs());
                UseCase useCase = useCaseRepository.findByUseCaseName(useCaseName).get();
                K8sObjectsManager.prepareS3MockYaml(configFilePath, useCase);
                log.debug("s3mock yaml file updated successfully");
                Thread.sleep(40 * 1000);
            } catch (Exception e) {
                log.error("Unable to create s3mock yaml file {}", useCaseName);
                throw new MayaServerException(e.getMessage());
            }
        }
    }
}
