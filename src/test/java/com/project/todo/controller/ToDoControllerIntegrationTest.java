package com.project.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.todo.dto.CreateToDoDto;
import com.project.todo.dto.ToDoDto;
import com.project.todo.dto.UpdateToDoDto;
import com.project.todo.dto.UserRegistrationDto;
import com.project.todo.service.JwtService;
import com.project.todo.service.ToDoService;
import com.project.todo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ToDoControllerIntegrationTest {

    public static final String OWNER_USERNAME = "usernameOwner";
    public static final String NOT_OWNER_USERNAME = "usernameNotOwner";
    public static final String PASSWORD = "password";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    //    @Autowired
//    private ToDoRepository toDoRepository;
    @Autowired
    private ToDoService toDoService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    //    private String token;
//    private HttpHeaders headers;
//    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        userService.createUser(new UserRegistrationDto(OWNER_USERNAME, PASSWORD));
        userService.createUser(new UserRegistrationDto(NOT_OWNER_USERNAME, PASSWORD));
    }


    @WithMockUser(username = OWNER_USERNAME)
    @Test
    void testGetAllToDos() throws Exception {
        mockMvc.perform(get("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getUserTokenHeaders(OWNER_USERNAME)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @WithMockUser(username = OWNER_USERNAME)
    @Test
    void testGetToDoById() throws Exception {
        ToDoDto toDoDto = createTestToDo();

        mockMvc.perform(get("/api/todo/{id}", toDoDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getUserTokenHeaders(OWNER_USERNAME)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(toDoDto.getId().intValue())));
    }


    @WithMockUser(username = NOT_OWNER_USERNAME)
    @Test
    void testGetToDoById_forbidden() throws Exception {
        ToDoDto toDoDto = createTestToDo();

        mockMvc.perform(get("/api/todo/{id}", toDoDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getUserTokenHeaders(OWNER_USERNAME)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message", containsString("Access Denied")));
    }


    @WithMockUser(username = OWNER_USERNAME)
    @Test
    void testCreateToDo() throws Exception {
        CreateToDoDto createToDoDto = new CreateToDoDto();
        createToDoDto.setDescription("Test ToDo");

        mockMvc.perform(post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getUserTokenHeaders(OWNER_USERNAME))
                        .content(objectMapper.writeValueAsString(createToDoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(createToDoDto.getDescription())));
    }

    @WithMockUser(username = OWNER_USERNAME)
    @Test
    void testCreateToDo_blankDescription() throws Exception {
        CreateToDoDto createToDoDto = new CreateToDoDto();
        createToDoDto.setDescription(" ");

        mockMvc.perform(post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getUserTokenHeaders(OWNER_USERNAME))
                        .content(objectMapper.writeValueAsString(createToDoDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Validation failed for one or several fields")));
    }

    @WithMockUser(username = OWNER_USERNAME)
    @Test
    void testCreateToDo_nullDescription() throws Exception {
        CreateToDoDto createToDoDto = new CreateToDoDto();
        createToDoDto.setDescription(null);

        mockMvc.perform(post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getUserTokenHeaders(OWNER_USERNAME))
                        .content(objectMapper.writeValueAsString(createToDoDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Validation failed for one or several fields")));
    }

    @WithMockUser(username = OWNER_USERNAME)
    @Test
    void testCreateToDo_shortDescription() throws Exception {
        CreateToDoDto createToDoDto = new CreateToDoDto();
        createToDoDto.setDescription("t");

        mockMvc.perform(post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getUserTokenHeaders(OWNER_USERNAME))
                        .content(objectMapper.writeValueAsString(createToDoDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Validation failed for one or several fields")));
    }

    @WithMockUser(username = NOT_OWNER_USERNAME)
    @Test
    void testUpdateToDo_forbidden() throws Exception {
        ToDoDto toDoDto = createTestToDo();
        UpdateToDoDto updateToDoDto = new UpdateToDoDto();
        updateToDoDto.setDescription("Updated ToDo");

        mockMvc.perform(patch("/api/todo/{id}", toDoDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getUserTokenHeaders(OWNER_USERNAME))
                        .content(objectMapper.writeValueAsString(updateToDoDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message", containsString("Access Denied")));
    }

    @WithMockUser(username = OWNER_USERNAME)
    @Test
    void testUpdateToDo() throws Exception {
        ToDoDto toDoDto = createTestToDo();
        UpdateToDoDto updateToDoDto = new UpdateToDoDto();
        updateToDoDto.setDescription("Updated ToDo");

        mockMvc.perform(patch("/api/todo/{id}", toDoDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getUserTokenHeaders(OWNER_USERNAME))
                        .content(objectMapper.writeValueAsString(updateToDoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(updateToDoDto.getDescription())));
    }

    @WithMockUser(username = OWNER_USERNAME)
    @Test
    void testUpdateToDo_notFound() throws Exception {
        ToDoDto toDoDto = createTestToDo();
        UpdateToDoDto updateToDoDto = new UpdateToDoDto();
        updateToDoDto.setDescription("Updated ToDo");

        mockMvc.perform(patch("/api/todo/{id}", toDoDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getUserTokenHeaders(OWNER_USERNAME))
                        .content(objectMapper.writeValueAsString(updateToDoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(updateToDoDto.getDescription())));
    }

    @WithMockUser(username = OWNER_USERNAME)
    @Test
    void testUpdateToDo_blankDescription() throws Exception {
        ToDoDto toDoDto = createTestToDo();
        UpdateToDoDto updateToDoDto = new UpdateToDoDto();
        updateToDoDto.setDescription(" ");

        mockMvc.perform(patch("/api/todo/{id}", toDoDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getUserTokenHeaders(OWNER_USERNAME))
                        .content(objectMapper.writeValueAsString(updateToDoDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Validation failed for one or several fields")));
    }

    @WithMockUser(username = OWNER_USERNAME)
    @Test
    void testUpdateToDo_nullDescription() throws Exception {
        ToDoDto toDoDto = createTestToDo();
        UpdateToDoDto updateToDoDto = new UpdateToDoDto();
        updateToDoDto.setDescription(null);

        mockMvc.perform(patch("/api/todo/{id}", toDoDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getUserTokenHeaders(OWNER_USERNAME))
                        .content(objectMapper.writeValueAsString(updateToDoDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Validation failed for one or several fields")));
    }

    @WithMockUser(username = OWNER_USERNAME)
    @Test
    void testUpdateToDo_shortDescription() throws Exception {
        ToDoDto toDoDto = createTestToDo();
        UpdateToDoDto updateToDoDto = new UpdateToDoDto();
        updateToDoDto.setDescription("t");

        mockMvc.perform(patch("/api/todo/{id}", toDoDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getUserTokenHeaders(OWNER_USERNAME))
                        .content(objectMapper.writeValueAsString(updateToDoDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Validation failed for one or several fields")));
    }

    @WithMockUser(username = OWNER_USERNAME)
    @Test
    void testUpdateToDo_tooLongDescription() throws Exception {
        ToDoDto toDoDto = createTestToDo();
        UpdateToDoDto updateToDoDto = new UpdateToDoDto();
        updateToDoDto.setDescription("a".repeat(256));

        mockMvc.perform(patch("/api/todo/{id}", toDoDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getUserTokenHeaders(OWNER_USERNAME))
                        .content(objectMapper.writeValueAsString(updateToDoDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Validation failed for one or several fields")));
    }

    @WithMockUser(username = NOT_OWNER_USERNAME)
    @Test
    void testDeleteToDo_forbidden() throws Exception {
        ToDoDto toDoDto = createTestToDo();

        mockMvc.perform(delete("/api/todo/{id}", toDoDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getUserTokenHeaders(OWNER_USERNAME)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message", containsString("Access Denied")));
    }

    @WithMockUser(username = OWNER_USERNAME)
    @Test
    void testDeleteToDo() throws Exception {
        ToDoDto toDoDto = createTestToDo();

        mockMvc.perform(delete("/api/todo/{id}", toDoDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getUserTokenHeaders(OWNER_USERNAME)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/todo/{id}", toDoDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getUserTokenHeaders(OWNER_USERNAME)))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = OWNER_USERNAME)
    @Test
    void testDeleteToDo_NotFound() throws Exception {
        long nonExistentId = 999L;

        mockMvc.perform(delete("/api/todo/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(getUserTokenHeaders(OWNER_USERNAME)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("ToDo not found")));
    }

    private HttpHeaders getUserTokenHeaders(String username) {
        String token = jwtService.generateToken(username);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }

    private ToDoDto createTestToDo() {
        CreateToDoDto createToDoDto = new CreateToDoDto();
        createToDoDto.setDescription("Test ToDo");
        return toDoService.createToDo(createToDoDto);
    }
}