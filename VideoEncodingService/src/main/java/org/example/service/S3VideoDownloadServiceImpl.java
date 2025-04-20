package org.example.service;

import org.example.pojo.VideoEncodingMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service("s3-download")
public class S3VideoDownloadServiceImpl implements IVideoDownloadService <VideoEncodingMessage> {

    @Autowired
    private S3Client s3Client;

    @Value("${video.local.temporary.dir}")
    private String videoLocalTempDir;

    private String videoFileName;

    @Override
    public File download(VideoEncodingMessage videoEncodingMessage) throws IOException {
        if (videoEncodingMessage == null) {
            throw new IllegalArgumentException("Info to download file from S3 is not available.");
        }
        generateFileName(videoEncodingMessage);
        String s3Key = videoEncodingMessage.getS3Key();
        Path localPath = Paths.get(videoLocalTempDir, this.videoFileName);
        Files.createDirectories(localPath.getParent());
        try {
            GetObjectRequest getReq = GetObjectRequest.builder()
                    .bucket(videoEncodingMessage.getS3BucketName())
                    .key(s3Key)
                    .build();
            GetObjectResponse getResp = s3Client.getObject(getReq, ResponseTransformer.toFile(localPath));
            return localPath.toFile();
        } catch (S3Exception e) {
            throw new RuntimeException("S3 Download failed: " + e.awsErrorDetails().errorMessage(), e);
        }
    }

    private void generateFileName(VideoEncodingMessage VideoEncodingMessage) {
        String videoId = VideoEncodingMessage.getVideoId();
        String fileExtension = VideoEncodingMessage.getExtension();
        this.videoFileName = String.format("%s%s%s", videoId, ".", fileExtension);
    }

}
