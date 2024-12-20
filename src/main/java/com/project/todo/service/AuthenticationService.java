package com.project.todo.service;

import com.project.todo.dto.TokenResponseDto;
import com.project.todo.service.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final JwtService jwtService;

    public TokenResponseDto authenticateUser(Authentication authentication) {
        if (authentication.isAuthenticated()) {
            log.debug("User authenticated successfully with username: {}", authentication.getName());
            return new TokenResponseDto(jwtService.generateToken(authentication.getName()));
        }
        throw new ApplicationException("Authentication failed", HttpStatus.UNAUTHORIZED);
    }
}
