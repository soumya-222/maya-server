package com.opentext.mayaserver.datauploaders.s3mock;


import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.opentext.mayaserver.utility.Constants.*;

/**
 * @author Rajiv
 */

@Slf4j
@Component
public class AwsS3ClientFactory {

    private final String ACCESS_KEY = "Dummy";
    private final String SECRET_KEY = "DummyKey";
    private final String PORT = "9090";

    public AmazonS3 getAmazonS3Client(String useCaseName) {
        String serviceName = S3MOCK_POD_SUFFIX + "-" + useCaseName + "-svc";
        String endpoint = HTTP + serviceName + ":9090" + "/" + useCaseName;
        BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
                .withPathStyleAccessEnabled(true)
                .withChunkedEncodingDisabled(true)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(new ClientConfiguration());
        builder.setEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, REGION));
        return builder.build();
    }
}
