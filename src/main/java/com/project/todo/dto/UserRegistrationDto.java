package com.project.todo.dto;

import com.project.todo.domain.User;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {
    @Size(min = 2, max = 50)
    private String username;
    @Size(min = 8, max = 255)
    private String password;

    public User toUser() {
        final User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }
}
