package com.project.todo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtService {

    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.ttl}")
    private Duration ttl;
    private final JwtEncoder jwtEncoder;

    public String generateToken(String username) {
        log.debug("Generating token for username: {}", username);
        final Instant now = Instant.now();
        final JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plus(ttl))
                .subject(username)
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
        log.debug("Token generated successfully for username: {}", username);
        return token;
    }
}