package com.opentext.mayaserver.config;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Rajiv
 */
@Data
public class AzuriteConfig {

    @NotNull
    private String nfs;
}
