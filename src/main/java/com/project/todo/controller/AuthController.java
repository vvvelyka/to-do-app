package com.project.todo.controller;

import com.project.todo.dto.UserRegistrationDto;
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
@RequestMapping("/api")
@RestController
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signup(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        log.info("Signup request received for username: {}", userRegistrationDto.getUsername());
        return userService.createUser(userRegistrationDto);
    }

    @PostMapping("/login")
    public String login(Authentication authentication) {
        log.info("Login request received for username: {}", authentication.getName());
        return userService.authenticateUser(authentication);
    }
}