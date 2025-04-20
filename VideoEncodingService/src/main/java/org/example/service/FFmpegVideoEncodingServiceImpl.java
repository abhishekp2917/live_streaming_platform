package org.example.service;

import org.example.pojo.VideoEncodingProfile;
import org.example.task.VideoEncodingTask;
import org.example.util.FfmpegCommandBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@Service("ffmpeg-encoder")
public class FFmpegVideoEncodingServiceImpl implements IVideoEncodingService {

    @Value("${video.encoded.local.temporary.dir}")
    private String encodedVideoLocalTempDir;

    @Autowired
    private FfmpegCommandBuilder ffmpegCommandBuilder;

    private final List<VideoEncodingProfile> videoEncodingProfiles = List.of(
            new VideoEncodingProfile("426x240", "400k", "64k", 2),
            new VideoEncodingProfile("640x360", "800k", "96k", 2),
            new VideoEncodingProfile("854x480", "1200k", "128k", 2),
            new VideoEncodingProfile("1280x720", "2000k", "128k", 2)
    );

    @Override
    public void encode(File videoFile) throws Exception {
        final String videoId = getVideoId(videoFile);
        File outputDir = prepareOutputDirectory(videoId);
        performEncodingInParallel(videoFile, outputDir);
        generateMasterPlaylist(outputDir, videoEncodingProfiles);
    }

    private String getVideoId(File videoFile) {
        String fileName = videoFile.getName();
        int dot = fileName.lastIndexOf('.');
        return (dot > 0) ? fileName.substring(0, dot) : fileName;
    }

    private File prepareOutputDirectory(String videoId) {
        File outputDir = new File(this.encodedVideoLocalTempDir.replace("{videoId}", videoId));
        if (!outputDir.exists()) outputDir.mkdirs();
        return outputDir;
    }

    private void performEncodingInParallel(File videoFile, File outputDir) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(videoEncodingProfiles.size());
        List<Future<File>> futures = new ArrayList<>();
        videoEncodingProfiles.stream().forEach(profile -> {
            File encodedVideoOutputDir = new File(outputDir, profile.getResolution());
            if (!encodedVideoOutputDir.exists()) encodedVideoOutputDir.mkdirs();

            Callable<File> task = new VideoEncodingTask(videoFile, profile, ffmpegCommandBuilder, encodedVideoOutputDir.getAbsolutePath());
            futures.add(executor.submit(task));
        });
        for (Future<File> future : futures) {
            future.get();
        }
        executor.shutdown();
    }

    private void generateMasterPlaylist(File outputDir, List<VideoEncodingProfile> videoEncodingProfiles) throws IOException {
        File masterPlaylist = new File(outputDir, "master.m3u8");
        try (FileWriter writer = new FileWriter(masterPlaylist)) {
            writer.write("#EXTM3U\n");
            for (VideoEncodingProfile profile : videoEncodingProfiles) {
                String resolution = profile.getResolution();
                String bandwidth = profile.getVideoBitrate().replace("k", "000");
                writer.write(String.format("#EXT-X-STREAM-INF:BANDWIDTH=%s,RESOLUTION=%s\n", bandwidth, resolution));
                writer.write(String.format("%s/%s.m3u8\n", resolution, resolution));  // assumes segments are named as <resolution>.m3u8
            }
        }
    }
}
