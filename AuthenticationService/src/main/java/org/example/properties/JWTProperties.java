package org.example.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTProperties {

    private String secretKey;
    private String scope;
    private String subject;
    private long expiration;
    private String tokenType;
}
