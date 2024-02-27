package com.opentext.mayaserver.config;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RecommendationConfig {

    @NotNull
    private String nfsDataPath;
    private String nfsConfigPath;
}
