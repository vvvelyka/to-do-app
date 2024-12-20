package com.project.todo.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {
    @Size(min = 2, max = 50)
    private String username;
    @Size(min = 8, max = 255)
    private String password;
}
