package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuccessfulLoginResponseDTO implements ILoginResponseDTO {

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private long expiresIn;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("scope")
    private String scope;
}
