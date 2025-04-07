package org.example.model;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoMetadata implements Serializable {

    private String videoId;
    private String fileName;
    private String fileFormat;
    private long fileSize;
    private String storagePath;
    private String userId;
    private LocalDateTime uploadTimestamp;

}

