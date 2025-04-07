package org.example.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VideoEncodingMessage implements Serializable {
    private String videoId;
    private String s3Key;
    private String bucketName;
    private String region;
    private String userId;
}
