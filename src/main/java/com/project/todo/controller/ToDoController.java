package com.project.todo.controller;

import com.project.todo.domain.ToDo;
import com.project.todo.service.ToDoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/todo")
@RestController
public class ToDoController {

    private ToDoService toDoService;

    @GetMapping
    public List<ToDo> getAllToDos() {
        log.info("Fetching all ToDos");
        return toDoService.getAllToDos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToDo> getToDoById(@PathVariable final Long id) {
        log.info("Fetching ToDo with id: {}", id);
        final Optional<ToDo> toDo = toDoService.getToDoById(id);
        return toDo.map(ResponseEntity::ok).orElseGet(() -> {
            log.warn("ToDo with id: {} not found", id);
            return ResponseEntity.notFound().build();
        });
    }

    @PostMapping
    public ToDo createToDo(@RequestBody final ToDo toDo) {
        log.info("Creating new ToDo: {}", toDo);
        return toDoService.createToDo(toDo);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ToDo> updateToDo(@PathVariable final Long id, @RequestBody final ToDo changedToDo) {
        log.info("Updating ToDo with id: {}", id);
        final Optional<ToDo> updatedToDo = toDoService.updateToDo(id, changedToDo);
        return updatedToDo.map(ResponseEntity::ok).orElseGet(() -> {
            log.warn("ToDo with id: {} not found", id);
            return ResponseEntity.notFound().build();
        });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToDo(@PathVariable final Long id) {
        log.info("Deleting ToDo with id: {}", id);
        if (toDoService.deleteToDo(id)) {
            log.info("Deleted ToDo with id: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("ToDo with id: {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }
}