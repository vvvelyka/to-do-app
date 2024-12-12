package com.project.todo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Table(name = "todo")
@Entity
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String description;
    private Boolean checkMark = false;
    private LocalDateTime completionDate;
    private LocalDateTime dueDate;
    private LocalDateTime creationDate;
}