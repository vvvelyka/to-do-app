package com.project.todo.controller;

import com.project.todo.dto.UserRegistrationDto;
import com.project.todo.service.JwtService;
import com.project.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public String signup(@RequestBody UserRegistrationDto userRegistrationDto) {
        return userService.createUser(userRegistrationDto);
    }

    @PostMapping("/login")
    public String login(final Authentication authentication) {
        return jwtService.generateToken(authentication.getName());
    }
}
