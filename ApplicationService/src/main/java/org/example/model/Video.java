package org.example.model;

import lombok.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.example.enums.VideoEncodingStatus;
import org.example.pojo.Resolution;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "videos")
public class Video {

    @Id
    private String videoId;
    private String userId;
    private String s3BucketName;
    private String s3Region;
    private String s3RawPath;
    private String s3EncodedPath;
    private String extension;
    private Resolution resolution;
    @Enumerated(EnumType.STRING)
    private VideoEncodingStatus encodingStatus;
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
    private LocalDateTime timestamp;
}
