package org.example.util;

import org.example.pojo.VideoEncodingProfile;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class FfmpegCommandBuilder {

    public List<String> buildCommand(File videoFile, VideoEncodingProfile videoEncodingProfile, String outputManifestPath) {
        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-i");
        command.add(videoFile.getAbsolutePath());
        // Scale video
        command.add("-vf");
        command.add("scale=" + videoEncodingProfile.getResolution());
        // Video encoding
        command.add("-c:v");
        command.add("libx264");
        command.add("-b:v");
        command.add(videoEncodingProfile.getVideoBitrate());
        // Audio encoding
        command.add("-c:a");
        command.add("aac");
        command.add("-b:a");
        command.add(videoEncodingProfile.getAudioBitrate());
        // HLS settings
        command.add("-f");
        command.add("hls");
        command.add("-hls_time");
        command.add(String.valueOf(videoEncodingProfile.getSegmentTime()));
        command.add("-hls_playlist_type");
        command.add("vod");
        // Output path
        command.add(outputManifestPath);
        return command;
    }
}
