package com.opentext.mayaserver.datagenerators.aws.recommendation.model;

import java.util.List;

public enum AwsSavingsPlanRequestPlanType {
    COMPUTE_SP("CO"),
    EC2_INSTANCE_SP("EC"),
    SAGEMAKER_SP("SA");

    private String shortName;

    AwsSavingsPlanRequestPlanType(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public static List<AwsSavingsPlanRequestPlanType> enabled() {
        return List.of(COMPUTE_SP, EC2_INSTANCE_SP, SAGEMAKER_SP);
    }
}
