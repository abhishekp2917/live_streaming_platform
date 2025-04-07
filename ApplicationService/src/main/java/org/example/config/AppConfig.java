package org.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@ComponentScan("ord.example")
public class AppConfig {

    @Autowired
    private Environment environment;

    @Bean("s3Config")
    public S3Config getS3Config() {
        return S3Config.builder()
                .accessKey(environment.getProperty("aws.s3.accessKey"))
                .secretKey(environment.getProperty("aws.s3.secretKey"))
                .region(environment.getProperty("aws.s3.region"))
                .bucketName(environment.getProperty("aws.s3.bucketName"))
                .targetPath(environment.getProperty("aws.s3.video.raw.targetPath"))
                .build();
    }


    @Bean("s3Client")
    @DependsOn("s3Config")
    public S3Client getS3Client() {
        return S3Client.builder()
                .region(Region.of(getS3Config().getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(getS3Config().getAccessKey(), getS3Config().getSecretKey())
                ))
                .build();
    }
}
