package com.opentext.mayaserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

/**
 * @author Rajiv
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "maya-server")
public class ApplicationPropertiesConfig {

    @NotNull
    private String host;

    @NotNull
    private S3MockConfig s3Mock;

    @NotNull
    private MockoonConfig mockoon;

    @NotNull
    private RecommendationConfig recommendation;

    @NotNull
    private AzuriteConfig azurite;

    @NotNull
    private String byodDataPath;
}
