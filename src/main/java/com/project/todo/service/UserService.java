package com.project.todo.service;

import com.project.todo.dto.UserRegistrationDto;
import com.project.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public String createUser(final UserRegistrationDto userRegistrationDto) {
        if(userRepository.existsByUsername(userRegistrationDto.getUsername())) {
            throw new IllegalArgumentException("User already exists");
        }
        final String username = userRepository.save(userRegistrationDto.toUser()).getUsername();
        return jwtService.generateToken(username);
    }

}
