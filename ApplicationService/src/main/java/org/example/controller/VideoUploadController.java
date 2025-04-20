package org.example.controller;

import org.example.annotation.ValidateVideo;
import org.example.dto.FailedVideoUploadResponseDTO;
import org.example.dto.IVideoUploadResponseDTO;
import org.example.dto.SuccessfulVideoUploadResponseDTO;
import org.example.model.Video;
import org.example.service.IVideoUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/video")
public class VideoUploadController {

    @Autowired
    private IVideoUploadService <Video> videoUploadService;


    @PostMapping("/upload")
    @ValidateVideo
    public ResponseEntity<IVideoUploadResponseDTO> uploadVideo(@RequestPart("file") MultipartFile file) {
        IVideoUploadResponseDTO responseDTO = null;
        try {
            Video video = videoUploadService.upload(file);
            if(video!=null && video.getVideoId()!=null) {
                responseDTO = SuccessfulVideoUploadResponseDTO.builder()
                        .videoID(video.getVideoId())
                        .build();
                return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
            }
            else {
                responseDTO = FailedVideoUploadResponseDTO.builder()
                        .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .errorDescription("Failed to upload video.")
                        .build();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
            }
        } catch (IOException e) {
            responseDTO = FailedVideoUploadResponseDTO.builder()
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errorDescription(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}
