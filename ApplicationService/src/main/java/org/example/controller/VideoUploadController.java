package org.example.controller;

import org.example.annotation.ValidateVideo;
import org.example.service.IVideoUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/videos")
public class VideoUploadController {

    @Autowired
    private IVideoUploadService videoUploadService;

    @PostMapping("/upload")
    @ValidateVideo()
    public ResponseEntity<String> uploadVideo(@RequestPart("file") MultipartFile file) {
        try {
            boolean uploaded = videoUploadService.upload(file);
            if(uploaded) {
                return ResponseEntity.ok().body("Video upload successfully!");
            }
            else {
                return ResponseEntity.internalServerError().body("Upload failed!");
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }
}
