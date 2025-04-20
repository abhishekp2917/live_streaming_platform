package org.example.service;

import org.example.properties.S3Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import java.time.Duration;

@Service("S3PreSignedVideoStreamUrlService")
public class GenerateS3PreSignedVideoStreamUrlServiceImpl implements IGenerateVideoStreamURLService {

    @Autowired
    private S3Presigner s3Presigner;

    @Autowired
    private S3Properties s3Properties;

    public String generateUrl(String videoId) {
        String s3Key = String.format("%s/%s/%s.m3u8", s3Properties.getEncodedVideoPath(), videoId, videoId);
        Duration expiration = Duration.ofMinutes(60);
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3Properties.getBucketName())
                .key(s3Key)
                .build();
        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(p -> p
                .getObjectRequest(getObjectRequest)
                .signatureDuration(expiration));
        return presignedRequest.url().toString();
    }

}
