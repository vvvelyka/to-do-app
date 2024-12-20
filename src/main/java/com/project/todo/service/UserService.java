package com.project.todo.service;

import com.project.todo.domain.User;
import com.project.todo.dto.UserDto;
import com.project.todo.dto.UserRegistrationDto;
import com.project.todo.repository.UserRepository;
import com.project.todo.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public UserDto createUser(UserRegistrationDto userRegistrationDto) {
        log.info("Creating user with username: {}", userRegistrationDto.getUsername());
        if (userRepository.existsByUsername(userRegistrationDto.getUsername())) {
            log.warn("User with username: {} already exists", userRegistrationDto.getUsername());
            throw new IllegalArgumentException("User already exists");
        }
        User savedUser = saveUser(userRegistrationDto);
        log.info("User created successfully with username: {}", savedUser.getUsername());
        return userMapper.toDto(savedUser);
    }

    public String authenticateUser(Authentication authentication) {
        if (authentication.isAuthenticated()) {
            log.info("User authenticated successfully with username: {}", authentication.getName());
            return jwtService.generateToken(authentication.getName());
        }
        log.warn("Authentication failed for user: {}", authentication.getName());
        throw new IllegalArgumentException("Authentication failed");
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        log.info("Fetching current user with username: {}", username);
        return getUserByUsername(username);
    }

    private User saveUser(final UserRegistrationDto userRegistrationDto) {
        final User user = userMapper.toEntity(userRegistrationDto);
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        return userRepository.save(user);
    }
}