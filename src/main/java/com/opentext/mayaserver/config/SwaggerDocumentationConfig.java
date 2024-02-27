/*
 * Copyright 2020 - 2023 Open Text.
 *
 * The only warranties for products and services of Open Text and its affiliates and licensors ("Open Text")
 * are as may be set forth in the express warranty statements accompanying such products and services.
 * Nothing herein should be construed as constituting an additional warranty. Open Text shall not be liable
 * for technical or editorial errors or omissions contained herein. The information contained herein is subject to change without notice.
 *
 * Except as specifically indicated otherwise, this document contains confidential information and a valid
 * license is required for possession, use or copying. If this work is provided to the U.S. Government,
 * consistent with FAR 12.211 and 12.212, Commercial Computer Software, Computer Software
 * Documentation, and Technical Data for Commercial Items are licensed to the U.S. Government under
 * vendor's standard commercial license.
 */

package com.opentext.mayaserver.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerDocumentationConfig {

    @Bean
    public GroupedOpenApi contentStoreOpenApi() {
        String paths[] = {"/v1/**"};
        String packagesToscan[] = {"com.opentext.mayaserver.controllers"};
        return GroupedOpenApi.builder().group("controllers").pathsToMatch(paths).packagesToScan(packagesToscan)
                .build();
    }

}
