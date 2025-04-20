package org.example.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.exception.VideoFileEmptyException;
import org.example.exception.VideoFileTooLargeException;
import org.example.exception.UnsupportedVideoFormatException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class VideoValidationAspect {

    @Value("${video.max-file-size}")
    private long maxFileSize;

    @Value("${video.allowed-formats}")
    private String allowedFormats;

    @Before("@annotation(org.example.annotation.ValidateVideo)")
    public void validateVideoFile(JoinPoint joinPoint) {
        List<String> allowedVideoTypes = Arrays.stream(allowedFormats.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) arg;

                if (file == null || file.isEmpty()) {
                    throw new VideoFileEmptyException("Video file must not be empty.");
                }

                if (file.getSize() > maxFileSize) {
                    throw new VideoFileTooLargeException("Video file exceeds maximum size limit of " + maxFileSize + " bytes.");
                }

                if (!allowedVideoTypes.contains(file.getContentType())) {
                    throw new UnsupportedVideoFormatException("Unsupported video format: " + file.getContentType());
                }
            }
        }
    }
}
