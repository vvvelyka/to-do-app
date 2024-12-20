package com.project.todo.controller;

import com.project.todo.dto.CreateToDoDto;
import com.project.todo.dto.ToDoDto;
import com.project.todo.dto.UpdateToDoDto;
import com.project.todo.service.ToDoService;
import com.project.todo.service.exception.ApplicationException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for handling {@link com.project.todo.domain.ToDo}
 */
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/todo")
@RestController
public class ToDoController {

    private final ToDoService toDoService;


    /**
     * Retrieves all ToDo items for the current user.
     *
     * @return {@code 200} with list of {@link ToDoDto} in the body,
     *         {@code 401} if JWT invalid,
     *         {@code 500} if there is an internal server error.
     * @throws ApplicationException if there is an issue retrieving the ToDo items.
     */@GetMapping
    public List<ToDoDto> getAllToDos() {
        log.info("Request to get all ToDos for current user");
        return toDoService.getAllToDosForCurrentUser();
    }

    /**
     * Retrieves a ToDo by its ID.
     *
     * @param id the ID of the ToDo item to retrieve.
     * @return {@code 200} with the {@link ToDoDto},
     *         {@code 403} ToDo with the id does no belong current user,
     *         {@code 401} if JWT invalid,
     *         {@code 500} if there is an internal server error.
     * @throws ApplicationException if the ToDo item is not found.
     */
    @PreAuthorize("@authorizationService.validateAccess(#id)")
    @GetMapping("/{id}")
    public ToDoDto getToDoById(@PathVariable Long id) {
        log.info("Request to get ToDo with id: {}", id);
        return toDoService.getToDoById(id);
    }

    /**
     * Creates a new ToDo.
     *
     * @param toDoDto the data of ToDo to create.
     *         {@code 200} with the {@link ToDoDto} of the created ToDo,
     *         {@code 400} if provided data invalid,
     *         {@code 401} if JWT invalid,
     *         {@code 500} if there is an internal server error.
     */
    @PostMapping
    public ToDoDto createToDo(@RequestBody @Valid CreateToDoDto toDoDto) {
        log.info("Request to create new ToDo: {}", toDoDto);
        return toDoService.createToDo(toDoDto);
    }

    /**
     * Updates an existing ToDo.
     *
     * @param id the ID of the ToDo to update.
     * @param updatedToDo the data to update existing ToDo with.
     * @return {@code 200} with the {@link ToDoDto} of updated ToDo,
     *         {@code 400} if provided data invalid,
     *         {@code 404} ToDo not found,
     *         {@code 403} ToDo with the id does no belong to current user,
     *         {@code 401} if JWT invalid,
     *         {@code 500} if there is an internal server error.
     * @throws ApplicationException if the ToDo item is not found.
     * @throws ValidationException if the provided ToDo data is invalid.
     */
    @PreAuthorize("@authorizationService.validateAccess(#id)")
    @PatchMapping("/{id}")
    public ToDoDto updateToDo(@PathVariable Long id, @RequestBody @Valid UpdateToDoDto updatedToDo) {
        log.info("Request to update ToDo with id: {}", id);
        return toDoService.updateToDo(id, updatedToDo);
    }

    /**
     * Deletes a ToDo by its ID.
     *
     * @param id the ID of the ToDo to delete.
     * @return {@code 200} with no body,
     *         {@code 404} ToDo not found,
     *         {@code 403} ToDo with the id does no belong to current user,
     *         {@code 401} if JWT invalid,
     *         {@code 500} if there is an internal server error.
     */
    @PreAuthorize("@authorizationService.validateAccess(#id)")
    @DeleteMapping("/{id}")
    public void deleteToDo(@PathVariable Long id) {
        log.info("Request to delete ToDo with id: {}", id);
        toDoService.deleteToDo(id);
    }
}