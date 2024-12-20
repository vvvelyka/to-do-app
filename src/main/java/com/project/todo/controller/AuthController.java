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

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public UserDto signup(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        log.info("Signup request received for username: {}", userRegistrationDto.getUsername());
        return userService.createUser(userRegistrationDto);
    }

    @PostMapping("/login")
    public TokenResponseDto login(Authentication authentication) {
        log.info("Login request received for username: {}", authentication.getName());
        return authenticationService.authenticateUser(authentication);
    }
}