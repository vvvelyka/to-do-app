package com.project.todo.service;

import com.project.todo.domain.User;
import com.project.todo.dto.UserDto;
import com.project.todo.dto.UserRegistrationDto;
import com.project.todo.repository.UserRepository;
import com.project.todo.service.exception.ApplicationException;
import com.project.todo.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    public static final long ID = 1L;
    public static final String PASSWORD = "password";
    public static final String USERNAME = "username";
    public static final String ENCODED_PASSWORD = "encodedPassword";
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;
    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(ID);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);

        userDto = new UserDto();
        userDto.setId(ID);
        userDto.setUsername(USERNAME);

        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setUsername(USERNAME);
        userRegistrationDto.setPassword(PASSWORD);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createUser() {
        when(userMapper.toEntity(userRegistrationDto)).thenReturn(user);
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserDto result = userService.createUser(userRegistrationDto);

        assertNotNull(result);
        assertEquals(userDto, result);
    }

    @Test
    void createUser_UserAlreadyExists() {
        when(userMapper.toEntity(userRegistrationDto)).thenReturn(user);
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        ApplicationException exception = assertThrows(ApplicationException.class, () -> userService.createUser(userRegistrationDto));

        assertEquals("User already exists", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void getUserByUsername() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        User result = userService.getUserByUsername(USERNAME);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void getUserByUsername_NotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> userService.getUserByUsername(USERNAME));

        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void getCurrentUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        User result = userService.getCurrentUser();

        assertNotNull(result);
        assertEquals(user, result);
    }
}