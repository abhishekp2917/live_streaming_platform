package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuccessfulVideoStreamURLResponseDTO implements IVideoStreamURLResponseDTO {

    @JsonProperty("video_id")
    private String videoID;

    @JsonProperty("url")
    private String url;
}
