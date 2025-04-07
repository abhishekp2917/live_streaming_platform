package org.example.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class VideoValidationAspect {

    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024;

    private static final List<String> ALLOWED_VIDEO_TYPES = Arrays.asList(
            "video/mp4", "video/quicktime", "video/x-msvideo", "video/x-matroska"
    );

    @Before("@annotation(org.example.annotation.ValidateVideo)")
    public void validateVideoFile(JoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) arg;

                if (file == null || file.isEmpty()) {
                    throw new IllegalArgumentException("Video file must not be empty.");
                }

                if (file.getSize() > MAX_FILE_SIZE) {
                    throw new IllegalArgumentException("Video file exceeds maximum size limit of 10MB.");
                }

                if (!ALLOWED_VIDEO_TYPES.contains(file.getContentType())) {
                    throw new IllegalArgumentException("Unsupported video format: " + file.getContentType());
                }
            }
        }
    }
}
