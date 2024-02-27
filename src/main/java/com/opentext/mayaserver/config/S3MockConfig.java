package com.opentext.mayaserver.config;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class S3MockConfig {

    @NotNull
    private String nfs;
}
