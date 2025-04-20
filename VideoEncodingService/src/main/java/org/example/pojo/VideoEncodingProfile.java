package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VideoEncodingProfile {

    private String resolution;
    private String videoBitrate;
    private String audioBitrate;
    private int segmentTime;
}
