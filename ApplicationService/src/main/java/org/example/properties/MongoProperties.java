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
@ConfigurationProperties(prefix = "mongodb")
public class MongoProperties {

    private String uri;
    private String database;
    private String host;
    private int port;
    private String username;
    private String password;
    private String authDatabase;
    private String authMechanism;
}
