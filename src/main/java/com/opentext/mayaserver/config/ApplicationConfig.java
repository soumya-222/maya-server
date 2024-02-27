package com.opentext.mayaserver.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.opentext.mayaserver.utility.ProfileHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;

import static com.opentext.mayaserver.utility.Constants.*;

/**
 * @author Rajiv
 */

@Configuration
public class ApplicationConfig {

    public static final int QUEUE_CAPACITY = 25;
    public static final int POOL_SIZE = 20;
    private ApplicationPropertiesConfig applicationPropertiesConfig;

    private Environment environment;
    private String[] profiles;

    public ApplicationConfig(Environment environment,ApplicationPropertiesConfig applicationPropertiesConfig) {
        this.environment = environment;
        this.applicationPropertiesConfig = applicationPropertiesConfig;
    }

    @Bean
    ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(POOL_SIZE);
        threadPoolTaskExecutor.setMaxPoolSize(POOL_SIZE);
        threadPoolTaskExecutor.setQueueCapacity(QUEUE_CAPACITY);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(false);
        threadPoolTaskExecutor.setDaemon(true);
        threadPoolTaskExecutor.setThreadGroupName("MayaGroup");
        threadPoolTaskExecutor.setThreadNamePrefix("maya-");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @Bean
    ProfileHandler detectProfile() {
        ProfileHandler profileHandler = new ProfileHandler();
        profiles = environment.getActiveProfiles();
        if (Arrays.stream(profiles).anyMatch(profile -> profile.equalsIgnoreCase("test") || profile.equalsIgnoreCase("local"))) {
            profileHandler.setLocalOrTestProfileActive(true);
            profileHandler.setProductionProfileActive(false);
        } else {
            profileHandler.setProductionProfileActive(true);
            profileHandler.setLocalOrTestProfileActive(false);
        }
        return profileHandler;
    }

}
