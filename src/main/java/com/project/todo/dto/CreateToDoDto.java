package com.project.todo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateToDoDto {
    @NotNull(message = "Description cannot be null")
    @Size(min = 1, max = 255, message = "Description must be between 1 and 255 characters")
    private String description;
    private LocalDate dueDate;
}