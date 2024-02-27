package com.opentext.mayaserver.datagenerators.aws.recommendation.model;

import java.util.List;

public enum AwsSavingsPlanRequestPaymentOption {
    NO_UPFRONT("NO"),
    PARTIAL_UPFRONT("PA"),
    ALL_UPFRONT("AL");

    private String shortName;

    AwsSavingsPlanRequestPaymentOption(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public static List<AwsSavingsPlanRequestPaymentOption> enabled() {
        return List.of(ALL_UPFRONT, PARTIAL_UPFRONT, NO_UPFRONT);
    }
}
