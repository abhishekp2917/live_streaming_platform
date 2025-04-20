package org.example.config;

import org.example.service.IPasswordEncoder;
import org.example.service.SHA256PasswordEncoderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan("org.example")
public class AppConfig {

    @Bean
    public IPasswordEncoder customPasswordEncoder() {
        return new SHA256PasswordEncoderImpl();
    }
}
