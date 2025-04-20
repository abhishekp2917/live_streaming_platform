package org.example.config;

import org.example.filter.JWTTokenValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import java.util.Collections;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JWTTokenValidationFilter jwtTokenValidationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        configureSessionManagement(http);
        configureCORS(http);
        configureCSRF(http);
        configureFilters(http);
        configurePermitAllPermissions(http);
        configureAuthenticationType(http);
        return http.build();
    }

    private void configureSessionManagement(HttpSecurity http) throws Exception {
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }

    private void configureCORS(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource((request) -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
            corsConfiguration.setAllowCredentials(true);
            corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
            corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
            return corsConfiguration;
        }));
    }

    private void configureCSRF(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
    }

    private void configureFilters(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtTokenValidationFilter, BasicAuthenticationFilter.class);
    }

    private void configurePermitAllPermissions(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/video/**").authenticated()
                .anyRequest().denyAll()
        );
    }

    private void configureAuthenticationType(HttpSecurity http) throws Exception {
        http.httpBasic(httpBasic -> httpBasic.disable());
    }
}
