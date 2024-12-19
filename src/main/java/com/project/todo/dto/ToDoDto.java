package com.project.todo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ToDoDto {
    private Long id;
    private String description;
    private Boolean checkMark;
    private LocalDateTime completionDate;
    private LocalDate dueDate;
    private LocalDateTime creationDate;
    private Long userId;
}