package com.project.todo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Data
@Table(name = "todo")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String description;
    private Boolean checkMark = false;
    private LocalDateTime completionDate;
    private LocalDateTime dueDate;
    @CreatedDate
    private LocalDateTime creationDate;
}