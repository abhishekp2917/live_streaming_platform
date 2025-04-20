package org.example.service;

import org.example.properties.S3Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import java.net.URL;

@Service("S3PublicVideoStreamUrlService")
public class GenerateS3PublicVideoStreamUrlServiceImpl implements IGenerateVideoStreamURLService {

    @Autowired
    private S3Client s3Client;

    @Autowired
    private S3Properties s3Properties;

    public String generateUrl(String videoId) {
        String s3Key = String.format("%s/%s/master.m3u8", s3Properties.getEncodedVideoPath(), videoId);
        GetUrlRequest request = GetUrlRequest.builder()
                .bucket(s3Properties.getBucketName())
                .key(s3Key)
                .build();
        URL url = s3Client.utilities().getUrl(request);
        return url.toString();
    }
}
