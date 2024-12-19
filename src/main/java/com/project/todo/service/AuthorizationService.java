package com.project.todo.service;

import com.project.todo.dto.ToDoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthorizationService {
    private final UserService userService;
    private final ToDoService toDoService;

    public boolean validateAccess(Long toDoId) {
        Long currentUserId = userService.getCurrentUser().getId();
        ToDoDto toDo = toDoService.getToDoById(toDoId);
        return toDo.getUserId().equals(currentUserId);
    }
}
