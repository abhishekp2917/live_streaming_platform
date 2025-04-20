package org.example.service;

import org.example.producer.IVideoEncodingMQProducer;
import org.example.properties.S3Properties;
import org.example.enums.VideoEncodingStatus;
import org.example.model.Video;
import org.example.persistence.IVideoDbService;
import org.example.pojo.Resolution;
import org.example.pojo.VideoEncodingMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class S3VideoUploadServiceImpl implements IVideoUploadService <Video> {

    @Autowired
    public S3Properties s3Properties;

    @Autowired
    public S3Client s3Client;

    @Autowired
    private IVideoEncodingMQProducer videoEncodingMQService;

    @Autowired
    private IVideoDbService mongoVideoDbService;

    @Value("${video.local.temporary.dir}")
    private String VideoLocalTempDir;

    private String videoFileName;

    @Override
    public Video upload(MultipartFile file) throws IOException {
        File tempVideoFile = null;
        try {
            tempVideoFile = temporarilyStoreVideoFile(file);
            Video video = Video.builder()
                    .s3Region(s3Properties.getRegion())
                    .s3BucketName(s3Properties.getBucketName())
                    .s3RawPath(s3Properties.getRawVideoPath())
                    .s3EncodedPath(s3Properties.getEncodedVideoPath())
                    .encodingStatus(VideoEncodingStatus.PENDING)
                    .resolution(getVideoResolution(tempVideoFile))
                    .extension(getFileExtension(file.getOriginalFilename()))
                    .requestedAt(LocalDateTime.now())
                    .timestamp(LocalDateTime.now())
                    .build();
            video = mongoVideoDbService.save(video);
            generateFileName(video);
            String s3Key = generateS3Key(video);
            PutObjectRequest putReq = PutObjectRequest.builder()
                    .bucket(s3Properties.getBucketName())
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .build();
            PutObjectResponse putResp = s3Client.putObject(putReq, RequestBody.fromFile(tempVideoFile));
            VideoEncodingMessage message = VideoEncodingMessage.builder()
                    .videoId(video.getVideoId())
                    .userId(video.getUserId())
                    .s3Region(video.getS3Region())
                    .s3BucketName(video.getS3BucketName())
                    .s3RawPath(video.getS3RawPath())
                    .s3EncodedPath(video.getS3EncodedPath())
                    .s3Key(s3Key)
                    .extension(video.getExtension())
                    .resolution(video.getResolution()).build();
            videoEncodingMQService.publish(message);
            return video;
        }
        catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
        finally {
            if(tempVideoFile!=null) {
                tempVideoFile.delete();
            }
        }
    }

    private File temporarilyStoreVideoFile(MultipartFile file) throws IOException {
        File tempDir = new File(VideoLocalTempDir);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String tempFileName = String.format("%s%s%s", UUID.randomUUID(), ".", fileExtension);
        File tempVideoFile = new File(tempDir, tempFileName);
        if (tempVideoFile.exists()) {
            tempVideoFile.delete();
        }
        file.transferTo(tempVideoFile);
        return tempVideoFile;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private String generateS3Key(Video video) {
        String downloadPath = (video.getS3RawPath() != null ? video.getS3RawPath() + "/" : "");
        String s3Key = String.format("%s%s", downloadPath, this.videoFileName);
        return s3Key;
    }

    private void generateFileName(Video video) {
        String videoId = video.getVideoId();
        String fileExtension = video.getExtension();
        this.videoFileName = String.format("%s%s%s", videoId, ".", fileExtension);
    }

    private Resolution getVideoResolution(File videoFile) throws Exception {
        if (!videoFile.exists()) {
            throw new IllegalArgumentException("File does not exist: " + videoFile.getAbsolutePath());
        }
        String fileName = videoFile.getAbsolutePath();
        ProcessBuilder pb = new ProcessBuilder(
                "ffprobe",
                "-v", "error",
                "-select_streams", "v:0",
                "-show_entries", "stream=width,height",
                "-of", "csv=s=x:p=0",
                fileName
        );
        Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String resolutionLine = reader.readLine();
        int exitCode = process.waitFor();
        if (exitCode != 0 || resolutionLine == null) {
            throw new RuntimeException("Failed to get video resolution");
        }
        String[] parts = resolutionLine.split("x");
        int width = Integer.parseInt(parts[0]);
        int height = Integer.parseInt(parts[1]);
        return new Resolution(width, height);
    }
}
