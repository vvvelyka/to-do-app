package com.project.todo.service;

import com.project.todo.domain.ToDo;
import com.project.todo.dto.CreateToDoDto;
import com.project.todo.dto.ToDoDto;
import com.project.todo.dto.UpdateToDoDto;
import com.project.todo.repository.ToDoRepository;
import com.project.todo.service.mapper.ToDoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ToDoService {

    private final ToDoRepository toDoRepository;
    private final ToDoMapper toDoMapper;
    private final UserService userService;

    public List<ToDoDto> getAllToDosForCurrentUser() {
        log.info("Fetching all ToDos");
        return userService.getCurrentUser().getToDos().stream()
                .map(toDoMapper::toDto).toList();
    }

    public ToDoDto getToDoById(Long id) {
        log.info("Fetching ToDo with id: {}", id);
        return toDoRepository.findById(id)
                .map(toDoMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("ToDo not found"));
    }

    public ToDoDto createToDo(CreateToDoDto toDoDto) {
        log.info("Creating new ToDo: {}", toDoDto);
        ToDo toDo = toDoMapper.toEntity(toDoDto);
        toDo.setUser(userService.getCurrentUser());
        return toDoMapper.toDto(toDoRepository.save(toDo));
    }

    public ToDoDto updateToDo(Long id, UpdateToDoDto updateToDoDto) {
        log.info("Updating ToDo with id: {}", id);
        return toDoRepository.findById(id).map(toDo -> {
            if (updateToDoDto.getCheckMark() != null && !toDo.getCheckMark().equals(updateToDoDto.getCheckMark())) {
                updateCompletionDate(toDo, updateToDoDto);
            }
            toDoMapper.updateEntity(toDo, updateToDoDto);
            log.info("Updated ToDo: {}", toDo);
            return toDoMapper.toDto(toDoRepository.save(toDo));
        }).orElseThrow(() -> new IllegalArgumentException("ToDo not found"));
    }

    public void deleteToDo(Long id) {
        log.info("Deleting ToDo with id: {}", id);
        if (toDoRepository.existsById(id)) {
            toDoRepository.deleteById(id);
            log.info("Deleted ToDo with id: {}", id);
        } else {
            log.warn("ToDo with id: {} not found", id);
            throw new IllegalArgumentException("ToDo not found");
        }
    }

    private void updateCompletionDate(ToDo toDo, UpdateToDoDto updateToDoDto) {
        if (updateToDoDto.getCheckMark()) {
            toDo.setCompletionDate(LocalDateTime.now());
        } else {
            toDo.setCompletionDate(null);
        }
    }
}