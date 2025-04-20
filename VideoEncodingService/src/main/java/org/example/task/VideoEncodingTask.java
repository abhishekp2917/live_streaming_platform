package org.example.task;

import org.example.pojo.VideoEncodingProfile;
import org.example.util.FfmpegCommandBuilder;
import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

public class VideoEncodingTask implements Callable<File> {

    private File videoFile;
    private VideoEncodingProfile videoEncodingProfile;
    private FfmpegCommandBuilder ffmpegCommandBuilder;
    private String outputDir;


    public VideoEncodingTask(File videoFile, VideoEncodingProfile videoEncodingProfile, FfmpegCommandBuilder ffmpegCommandBuilder, String outputDir) {
        this.videoFile = videoFile;
        this.videoEncodingProfile = videoEncodingProfile;
        this.ffmpegCommandBuilder = ffmpegCommandBuilder;
        this.outputDir = outputDir;
    }


    @Override
    public File call() throws Exception {
        String res = this.videoEncodingProfile.getResolution();
        String manifestPath = new File(outputDir, res + ".m3u8").getAbsolutePath();
        List<String> command = this.ffmpegCommandBuilder.buildCommand(this.videoFile, videoEncodingProfile, manifestPath);
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.inheritIO();
        Process process = pb.start();
        int exit = process.waitFor();
        if (exit != 0) throw new RuntimeException("Encoding failed for: " + res);
        process.destroy();
        return new File(manifestPath);
    }
}
