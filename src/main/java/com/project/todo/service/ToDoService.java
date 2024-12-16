package com.project.todo.service;

import com.project.todo.domain.ToDo;
import com.project.todo.repository.ToDoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ToDoService {

    private final ToDoRepository toDoRepository;

    public List<ToDo> getAllToDos() {
        log.info("Fetching all ToDos");
        return (List<ToDo>) toDoRepository.findAll();
    }

    public Optional<ToDo> getToDoById(final Long id) {
        log.info("Fetching ToDo with id: {}", id);
        return toDoRepository.findById(id);
    }

    public ToDo createToDo(final ToDo toDo) {
        log.info("Creating new ToDo: {}", toDo);
        return toDoRepository.save(toDo);
    }

    public Optional<ToDo> updateToDo(final Long id, final ToDo toDoDetails) {
    log.info("Updating ToDo with id: {}", id);
    return toDoRepository.findById(id).map(toDo -> {
        if (toDoDetails.getDescription() != null) {
            toDo.setDescription(toDoDetails.getDescription());
        }
        if (toDoDetails.getDueDate() != null) {
            toDo.setDueDate(toDoDetails.getDueDate());
        }
        if (toDoDetails.getCreationDate() != null) {
            toDo.setCreationDate(toDoDetails.getCreationDate());
        }
        if (toDoDetails.getCheckMark() != null) {
            toDo.setCheckMark(toDoDetails.getCheckMark());
        }
        if (toDoDetails.getCompletionDate() != null) {
            toDo.setCompletionDate(toDoDetails.getCompletionDate());
        }
        log.info("Updated ToDo: {}", toDo);
        return toDoRepository.save(toDo);
    });
}

    public boolean deleteToDo(final Long id) {
        log.info("Deleting ToDo with id: {}", id);
        if (toDoRepository.existsById(id)) {
            toDoRepository.deleteById(id);
            log.info("Deleted ToDo with id: {}", id);
            return true;
        } else {
            log.warn("ToDo with id: {} not found", id);
            return false;
        }
    }
}