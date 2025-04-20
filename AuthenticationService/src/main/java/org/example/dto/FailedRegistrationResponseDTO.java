package org.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FailedRegistrationResponseDTO implements IRegistrationResponseDTO {

    @JsonProperty("error")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String error;

    @JsonProperty("error_description")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String errorDescription;

}
