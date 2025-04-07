package org.example.service;

import org.example.model.EncodedVideo;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FFmpegVideoEncodingServiceImpl implements IVideoEncodingService {

    @Override
    public EncodedVideo encode(File videoFile) throws Exception {
        // Temp Output Folder
        File outputDir = new File(System.getProperty("java.io.tmpdir"), "encoded_" + System.currentTimeMillis());
        outputDir.mkdirs();
        String outputManifestPath = new File(outputDir, "output.m3u8").getAbsolutePath();
        // FFmpeg command to generate HLS with multiple resolutions (ABR)
        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-i", videoFile.getAbsolutePath(),
                "-filter_complex",
                "[0:v]split=3[v1][v2][v3];" +
                        "[v1]scale=426:240[v1out];" +
                        "[v2]scale=640:360[v2out];" +
                        "[v3]scale=1280:720[v3out]",
                "-map", "[v1out]", "-b:v:0", "400k",
                "-map", "[v2out]", "-b:v:1", "800k",
                "-map", "[v3out]", "-b:v:2", "1200k",
                "-f", "hls",
                "-hls_time", "6",
                "-hls_playlist_type", "vod",
                outputManifestPath
        );
        pb.inheritIO();
        Process process = pb.start();
        int status = process.waitFor();
        if (status != 0) {
            throw new RuntimeException("Encoding Failed!");
        }
        // Create resolution -> File Map (if you want to store resolutions separately)
        Map<String, File> resolutionToFileMap = new HashMap<>();
        // HLS output will generate .ts segments & manifest (.m3u8)
        File manifestFile = new File(outputManifestPath);
        File[] segmentFiles = outputDir.listFiles((dir, name) -> name.endsWith(".ts"));
        if (segmentFiles != null) {
            for (File segment : segmentFiles) {
                // Optional: You can store based on resolution if filenames follow some pattern
                resolutionToFileMap.put(segment.getName(), segment);
            }
        }
        return new EncodedVideo(resolutionToFileMap, manifestFile, "HLS");
    }
}
