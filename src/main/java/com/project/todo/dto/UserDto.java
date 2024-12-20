package com.project.todo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String username;
    private LocalDateTime creationDate;
}
