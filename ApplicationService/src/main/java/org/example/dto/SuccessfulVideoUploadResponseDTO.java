package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuccessfulVideoUploadResponseDTO implements IVideoUploadResponseDTO {

    @JsonProperty("video_id")
    private String videoID;
}
