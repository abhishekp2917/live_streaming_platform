package org.example.pojo;

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
    private String userId;
    private String s3BucketName;
    private String s3Region;
    private String s3RawPath;
    private String s3EncodedPath;
    private String s3Key;
    private String extension;
    private Resolution resolution;
}
