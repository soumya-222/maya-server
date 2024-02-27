package com.opentext.mayaserver.datagenerators.aws.recommendation.model;

import java.util.List;

public enum AwsLookbackPeriod {
    SEVEN_DAYS("7d"),
    THIRTY_DAYS("30d"),
    SIXTY_DAYS("60d");

    private String shortName;

    AwsLookbackPeriod(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public static List<AwsLookbackPeriod> enabled() {
        return List.of(SEVEN_DAYS, THIRTY_DAYS, SIXTY_DAYS);
    }
}
