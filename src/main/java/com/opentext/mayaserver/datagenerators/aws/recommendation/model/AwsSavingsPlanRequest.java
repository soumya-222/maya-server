package com.opentext.mayaserver.datagenerators.aws.recommendation.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AwsSavingsPlanRequest {
    private AwsSavingsPlanRequestAccountScope accountScope;
    private AwsLookbackPeriod awsLookbackPeriod;
    private AwsSavingsPlanRequestPaymentOption awsSavingsPlanRequestPaymentOption;
    private AwsSavingsPlanRequestPlanType awsSavingsPlanRequestPlanType;
    private AwsSavingsPlanRequestTerm awsSavingsPlanRequestTerm;
}
