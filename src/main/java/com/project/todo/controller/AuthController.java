package com.project.todo.controller;

import com.project.todo.dto.TokenResponseDto;
import com.project.todo.dto.UserDto;
import com.project.todo.dto.UserRegistrationDto;
import com.project.todo.service.AuthenticationService;
import com.project.todo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling authentication-related requests.
 * Provides endpoints for user registration and login.
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    /**
     * Registers a new user.
     *
     * @param userRegistrationDto the user DTO with credentials to register.
     * @return {@code 200} with registered user details in the body,
     *         {@code 400} if the user already exists or provided data is invalid or,
     *         {@code 500} if there is an internal server error.
     */
    @PostMapping("/sign-up")
    public UserDto signup(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        log.info("Signup request received for username: {}", userRegistrationDto.getUsername());
        return userService.createUser(userRegistrationDto);
    }

    /**
     * Authenticates a user Basic HTTP authentication.
     *
     * @param authentication the authentication object containing the user's credentials.
     * @return {@code 200} with JWT access token in the body
     *         {@code 401} if the user credentials invalid
     *         {@code 500} if there is an internal server error.
     */
    @PostMapping("/login")
    public TokenResponseDto login(Authentication authentication) {
        log.info("Login request received for username: {}", authentication.getName());
        return authenticationService.authenticateUser(authentication);
    }
}