package com.project.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.todo.dto.TokenResponseDto;
import com.project.todo.dto.UserRegistrationDto;
import com.project.todo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerIntegrationTest {

    public static final String USERNAME = "testuser";
    public static final String PASSWORD = "password";
    public static final String VALIDATION_EXCEPTION_MESSAGE = "Validation failed for one or several fields";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserService userService;

    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        userRegistrationDto = createUserRegistrationDto();
    }

    @Test
    void testSignup() throws Exception {
        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(USERNAME)));
    }

    @Test
    void testSignup_UserAlreadyExists() throws Exception {
        userService.createUser(userRegistrationDto);

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("User already exists")));
    }

    @Test
    void testSignup_invalidUsername() throws Exception {
        userRegistrationDto.setUsername("");

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString(VALIDATION_EXCEPTION_MESSAGE)));
    }

    @Test
    void testSignup_invalidPassword() throws Exception {
        userRegistrationDto.setPassword("");

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString(VALIDATION_EXCEPTION_MESSAGE)));
    }

    @Test
    void testSignup_blankUsername() throws Exception {
        userRegistrationDto.setUsername(" ");

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString(VALIDATION_EXCEPTION_MESSAGE)));
    }

    @Test
    void testSignup_blankPassword() throws Exception {
        userRegistrationDto.setPassword(" ");

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString(VALIDATION_EXCEPTION_MESSAGE)));
    }

    @Test
    void testSignup_nullUsername() throws Exception {
        userRegistrationDto.setUsername(null);

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString(VALIDATION_EXCEPTION_MESSAGE)));
    }

    @Test
    void testSignup_nullPassword() throws Exception {
        userRegistrationDto.setPassword(null);

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString(VALIDATION_EXCEPTION_MESSAGE)));
    }

    @WithMockUser("spring")
    @Test
    void testLogin() throws Exception {

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationDto)))
                .andExpect(status().isOk())
                .andReturn();

        TokenResponseDto tokenResponse = objectMapper.readValue(result.getResponse().getContentAsString(), TokenResponseDto.class);

        assertNotNull(tokenResponse);
        assertNotNull(tokenResponse.accessToken());
        assertFalse(tokenResponse.accessToken().isEmpty());
    }

    private UserRegistrationDto createUserRegistrationDto() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setUsername(USERNAME);
        userRegistrationDto.setPassword(PASSWORD);
        return userRegistrationDto;
    }

}