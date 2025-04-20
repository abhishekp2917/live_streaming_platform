package org.example.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.model.User;
import org.example.properties.JWTProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTTokenGenerationServiceImpl implements ITokenGenerationService <User> {

    @Autowired
    private JWTProperties jwtProperties;

    @Override
    public String generateToken(User user) {
        if(user==null || user.getUsername()==null) {
            throw new IllegalArgumentException("Username is empty");
        }
        SecretKey key = Keys.hmacShaKeyFor(this.jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
        long expirationInMilliSeconds = this.jwtProperties.getExpiration()*1000;
        String jwtToken = Jwts.builder()
                .issuer("Abhishek")
                .subject(this.jwtProperties.getSubject())
                .claim("username", user.getUsername())
                .claim("scope", this.jwtProperties.getScope())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + expirationInMilliSeconds))
                .signWith(key)
                .compact();
        return jwtToken;
    }
}
