package org.example.controller;

import org.example.dto.FailedVideoStreamURLResponseDTO;
import org.example.dto.IVideoStreamURLResponseDTO;
import org.example.dto.SuccessfulVideoStreamURLResponseDTO;
import org.example.service.IGenerateVideoStreamURLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/video")
public class VideoStreamController {

    @Autowired
    @Qualifier("S3PublicVideoStreamUrlService")
    private IGenerateVideoStreamURLService generateVideoStreamUrlService;

    @GetMapping("/{videoId}/url")
    public ResponseEntity<IVideoStreamURLResponseDTO> getVideoStreamUrl(@PathVariable String videoId) {
        IVideoStreamURLResponseDTO responseDTO = null;
        try{
            String streamURL = generateVideoStreamUrlService.generateUrl(videoId);
            responseDTO = SuccessfulVideoStreamURLResponseDTO.builder()
                    .videoID(videoId)
                    .url(streamURL)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        }
        catch (Exception e) {
            responseDTO = FailedVideoStreamURLResponseDTO.builder()
                    .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errorDescription(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}
