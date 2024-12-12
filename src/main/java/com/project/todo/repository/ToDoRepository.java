package com.project.todo.repository;

import org.springframework.data.repository.CrudRepository;
import com.project.todo.domain.ToDo;

public interface ToDoRepository extends CrudRepository<ToDo, Long> {
}