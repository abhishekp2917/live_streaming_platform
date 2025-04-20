package org.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FailedLoginResponseDTO implements ILoginResponseDTO {

    @JsonProperty("error")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String error;

    @JsonProperty("error_description")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String errorDescription;

    @JsonProperty("error_code")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String errorCode;

    @JsonProperty("error_summary")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String errorSummary;

    @JsonProperty("error_link")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String errorLink;

    @JsonProperty("error_id")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String errorId;

    @JsonProperty("error_causes")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String[] errorCauses;
}
