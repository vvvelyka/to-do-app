package com.project.todo.dto;

import com.project.todo.domain.User;
import lombok.Data;

@Data
public class UserRegistrationDto {

    private String username;
    private String password;

    public User toUser() {
        final User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }
}
