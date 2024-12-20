package com.project.todo.service;

import com.project.todo.dto.TokenResponseDto;
import com.project.todo.service.exception.ApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    private static final String USERNAME = "username";
    private static final String TOKEN = "token";

    @Mock
    private JwtService jwtService;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void authenticateUser_success() {
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(USERNAME);
        when(jwtService.generateToken(USERNAME)).thenReturn(TOKEN);

        TokenResponseDto result = authenticationService.authenticateUser(authentication);

        assertNotNull(result);
        assertEquals(TOKEN, result.accessToken());
        verify(jwtService, times(1)).generateToken(USERNAME);
    }

    @Test
    void authenticateUser_failure() {
        when(authentication.isAuthenticated()).thenReturn(false);

        ApplicationException exception = assertThrows(ApplicationException.class, () -> authenticationService.authenticateUser(authentication));

        assertEquals("Authentication failed", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getHttpStatus());
        verify(jwtService, never()).generateToken(anyString());
    }
}