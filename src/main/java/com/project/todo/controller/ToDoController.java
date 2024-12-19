package com.project.todo.controller;

import com.project.todo.dto.CreateToDoDto;
import com.project.todo.dto.ToDoDto;
import com.project.todo.dto.UpdateToDoDto;
import com.project.todo.service.ToDoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/todo")
@RestController
public class ToDoController {

    private final ToDoService toDoService;

    @GetMapping
    public List<ToDoDto> getAllToDos() {
        log.info("Request to get all ToDos for current user");
        return toDoService.getAllToDosForCurrentUser();
    }

    @GetMapping("/{id}")
    public ToDoDto getToDoById(@PathVariable Long id) {
        log.info("Request to get ToDo with id: {}", id);
        return toDoService.getToDoById(id);
    }

    @PostMapping
    public ToDoDto createToDo(@RequestBody @Valid CreateToDoDto toDoDto) {
        log.info("Request to create new ToDo: {}", toDoDto);
        return toDoService.createToDo(toDoDto);
    }

    @PatchMapping("/{id}")
    public ToDoDto updateToDo(@PathVariable Long id, @RequestBody @Valid UpdateToDoDto updatedToDo) {
        log.info("Request to update ToDo with id: {}", id);
        return toDoService.updateToDo(id, updatedToDo);
    }

    @DeleteMapping("/{id}")
    public void deleteToDo(@PathVariable Long id) {
        log.info("Request to delete ToDo with id: {}", id);
        toDoService.deleteToDo(id);
    }
}