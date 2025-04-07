package org.example.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Builder
@Configuration
public class S3Config {

    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String region;
    private String targetPath;

}
