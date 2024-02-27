package com.opentext.mayaserver.datagenerators.aws.recommendation;

import com.opentext.mayaserver.config.ApplicationPropertiesConfig;
import com.opentext.mayaserver.datagenerators.aws.recommendation.model.RecommendationHolder;
import com.opentext.mayaserver.models.vo.UseCaseVO;
import com.opentext.mayaserver.services.DataGeneratorService;
import com.opentext.mayaserver.utility.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.opentext.mayaserver.utility.Constants.AWS_SP_RECOMMENDATION;
import static com.opentext.mayaserver.utility.Constants.AWS_SP_UTILIZATION;

@RequiredArgsConstructor
@Slf4j
@Component
public class AWSRecommendationAsyncGenerator {

    private final ApplicationPropertiesConfig applicationPropertiesConfig;
    private final DataGeneratorService dataGeneratorService;
    private final RecommendationHelper recommendationHelper;
    private final AWSRecommendationGenerator awsRecommendationGenerator;

    @Async
    public void generateRecommendations(UseCaseVO useCaseVO) {
        String useCaseName = useCaseVO.getUseCaseName();
        log.info("Generating AWS Recommendations for use case {}", useCaseName);
        CommonUtils.createDirectoryBasedOnUseCase(useCaseName, applicationPropertiesConfig.getRecommendation().getNfsDataPath());

        List<RecommendationHolder> recommendationHolders = recommendationHelper.getRecommendationHolders(useCaseName);
        awsRecommendationGenerator.generateAwsSpRecommendationApiResponses(
                dataGeneratorService.getAccountData(useCaseName), getTypedRecommendations(recommendationHolders, AWS_SP_RECOMMENDATION),
                useCaseVO.isDemoModeEnabled());
        awsRecommendationGenerator.generateAwsSpUtilizationApiResponses(getTypedRecommendations(recommendationHolders, AWS_SP_UTILIZATION));
        log.info("AWS Recommendations Data generated successfully");
    }

    private List<RecommendationHolder> getTypedRecommendations(List<RecommendationHolder> recommendationHolders, String recommendationType) {
        return recommendationHolders.stream().filter(recommendation -> recommendation.getEndpointType() == recommendationType).toList();
    }
}
