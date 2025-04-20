package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FailedVideoUploadResponseDTO implements IVideoUploadResponseDTO {

    @JsonProperty("error_code")
    private int errorCode;

    @JsonProperty("error_description")
    private String errorDescription;
}
