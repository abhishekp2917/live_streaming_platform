package org.example.service;

import org.example.config.S3Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Service
public class S3VideoUploadServiceImpl implements IVideoUploadService {

    @Autowired
    public S3Config config;

    @Autowired
    public S3Client s3Client;

    @Override
    public boolean upload(MultipartFile file) throws IOException {
        try {
            String fileName = Instant.now().toEpochMilli() + "-" + URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8);
            String s3Key = (config.getTargetPath() != null ? config.getTargetPath() + "/" : "") + fileName;

            PutObjectRequest putReq = PutObjectRequest.builder()
                    .bucket(config.getBucketName())
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .build();

            PutObjectResponse putResp = s3Client.putObject(putReq, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }
}
