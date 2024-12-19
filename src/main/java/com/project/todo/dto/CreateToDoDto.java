package com.project.todo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateToDoDto {
    @NotNull
    @Size(min = 1, max = 255)
    private String description;
    private LocalDate dueDate;
}