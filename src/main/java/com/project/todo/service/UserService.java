package com.project.todo.service;

import com.project.todo.domain.User;
import com.project.todo.dto.UserDto;
import com.project.todo.dto.UserRegistrationDto;
import com.project.todo.repository.UserRepository;
import com.project.todo.service.exception.ApplicationException;
import com.project.todo.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @Transactional
    public UserDto createUser(UserRegistrationDto userRegistrationDto) {
        log.debug("Creating user with username: {}", userRegistrationDto.getUsername());
        User user = userMapper.toEntity(userRegistrationDto);
        if (userRepository.existsByUsername(user.getUsername())) {
            log.warn("User with username: {} already exists", user.getUsername());
            throw new ApplicationException("User already exists", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        log.debug("User created successfully with username: {}", savedUser.getUsername());
        return userMapper.toDto(savedUser);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException("User not found", HttpStatus.NOT_FOUND));
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        log.debug("Fetching current user with username: {}", username);
        return getUserByUsername(username);
    }
}