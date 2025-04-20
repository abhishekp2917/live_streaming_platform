package org.example.service;

import org.example.pojo.VideoEncodingMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service("s3-upload")
public class S3EncodedVideoUploadServiceImpl implements IEncodedVideoUploadService <VideoEncodingMessage> {

    @Autowired
    public S3Client s3Client;

    @Value("${video.encoded.local.temporary.dir}")
    private String encodedVideoLocalTempDir;

    @Override
    public boolean upload(VideoEncodingMessage videoEncodingMessage) throws IOException {
        String videoId = videoEncodingMessage.getVideoId();
        String bucketName = videoEncodingMessage.getS3BucketName();
        String s3BaseKey = generateS3BaseKey(videoEncodingMessage);
        Path baseDirectoryPath = Path.of(this.encodedVideoLocalTempDir.replace("{videoId}", videoId));
        File baseDirectory = baseDirectoryPath.toFile();
        if (!baseDirectory.exists() || !baseDirectory.isDirectory()) {
            throw new IOException("Directory not found: " + baseDirectory.getAbsolutePath());
        }
        try {
            Files.walk(baseDirectoryPath)
                    .filter(Files::isRegularFile)
                    .forEach(filePath -> {
                        File file = filePath.toFile();
                        String relativeKeyPath = baseDirectoryPath.relativize(filePath).toString().replace("\\", "/");
                        String s3Key = String.format("%s/%s", s3BaseKey, relativeKeyPath);
                        String contentType = getContentType(file);
                        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                .bucket(bucketName)
                                .contentType(contentType)
                                .key(s3Key)
                                .build();
                        s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));
                    });
            return true;
        } catch (Exception exception) {
            throw new RuntimeException("Failed to upload files to S3: " + exception.getMessage(), exception);
        }
    }

    private String generateS3BaseKey(VideoEncodingMessage videoEncodingMessage) {
        String downloadPath = (videoEncodingMessage.getS3EncodedPath() != null ? videoEncodingMessage.getS3EncodedPath() : "");
        String s3BaseKey = String.format("%s/%s", downloadPath, videoEncodingMessage.getVideoId());
        return s3BaseKey;
    }

    private String getContentType(File file) {
        if(file == null) {
            throw new IllegalArgumentException("File doesn't exists");
        }
        String fileName = file.getName();
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            throw new IllegalArgumentException("File doesn't have an extension");
        }
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        switch (fileExtension) {
            case "m3u8" : return "application/x-mpegURL";
            case "ts" : return "video/MP2T";
            default: return "";
        }
    }
}
