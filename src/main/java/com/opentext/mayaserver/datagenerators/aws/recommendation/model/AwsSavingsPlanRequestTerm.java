package com.opentext.mayaserver.datagenerators.aws.recommendation.model;

import java.util.List;

public enum AwsSavingsPlanRequestTerm {
    ONE_YEAR("1y"),
    THREE_YEARS("3y");

    private String shortName;

    AwsSavingsPlanRequestTerm(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public static List<AwsSavingsPlanRequestTerm> enabled() {
        return List.of(ONE_YEAR, THREE_YEARS);
    }
}
