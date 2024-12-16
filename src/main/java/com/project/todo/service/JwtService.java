package com.project.todo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@RequiredArgsConstructor
@Component
public class JwtService {

    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.ttl}")
    private Duration ttl;
    private final JwtEncoder jwtEncoder;

    public String generateToken(final String username) {
        final Instant now = Instant.now();
        final JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plus(ttl))
                .subject(username)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
}
