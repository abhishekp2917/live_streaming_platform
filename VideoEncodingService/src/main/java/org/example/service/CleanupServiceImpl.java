package org.example.service;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.example.pojo.VideoEncodingMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class CleanupServiceImpl implements ICleanupService <VideoEncodingMessage> {

    @Value("${video.local.temporary.dir}")
    private String videoLocalTempDir;

    @Value("${video.encoded.local.temporary.dir}")
    private String encodedVideoLocalTempDir;

    @Override
    public void cleanUp(VideoEncodingMessage videoEncodingMessage) throws IOException {
        deleteDownloadedVideo(videoEncodingMessage);
        deleteEncodedVideo(videoEncodingMessage);
    }

    private void deleteDownloadedVideo(VideoEncodingMessage videoEncodingMessage) throws IOException {
        Path path = Path.of(String.format("%s/%s.%s", this.videoLocalTempDir, videoEncodingMessage.getVideoId(), videoEncodingMessage.getExtension()));
        Files.deleteIfExists(path);
    }

    private void deleteEncodedVideo(VideoEncodingMessage videoEncodingMessage) throws IOException {
        String videoId = videoEncodingMessage.getVideoId();
        Path encodedVideoDirectoryPath = Path.of(encodedVideoLocalTempDir.replace("{videoId}", videoId));
        File encodedVideoDirectory = encodedVideoDirectoryPath.toFile();
        FileUtils.deleteDirectory(encodedVideoDirectory);
    }
}
