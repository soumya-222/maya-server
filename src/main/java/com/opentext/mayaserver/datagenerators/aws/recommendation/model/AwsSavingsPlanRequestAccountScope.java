package com.opentext.mayaserver.datagenerators.aws.recommendation.model;

import java.util.List;

public enum AwsSavingsPlanRequestAccountScope {
    LINKED("LI"),
    PAYER("PA");

    private String shortName;

    AwsSavingsPlanRequestAccountScope(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public static List<AwsSavingsPlanRequestAccountScope> enabled() {
        return List.of(LINKED, PAYER);
    }
}
