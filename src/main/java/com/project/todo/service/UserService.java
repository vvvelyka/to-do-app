package com.project.todo.service;

import com.project.todo.domain.User;
import com.project.todo.dto.UserRegistrationDto;
import com.project.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public String createUser(final UserRegistrationDto userRegistrationDto) {
        log.info("Creating user with username: {}", userRegistrationDto.getUsername());
        if (userRepository.existsByUsername(userRegistrationDto.getUsername())) {
            log.warn("User with username: {} already exists", userRegistrationDto.getUsername());
            throw new IllegalArgumentException("User already exists");
        }
        final User user = saveUser(userRegistrationDto);
        String token = jwtService.generateToken(user.getUsername());
        log.info("User created successfully with username: {}", user.getUsername());
        return token;
    }

    private User saveUser(final UserRegistrationDto userRegistrationDto) {
        final User user = userRegistrationDto.toUser();
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        return userRepository.save(user);
    }
}