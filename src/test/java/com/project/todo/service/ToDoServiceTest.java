package com.project.todo.service;

import com.project.todo.domain.ToDo;
import com.project.todo.domain.User;
import com.project.todo.dto.CreateToDoDto;
import com.project.todo.dto.ToDoDto;
import com.project.todo.dto.UpdateToDoDto;
import com.project.todo.repository.ToDoRepository;
import com.project.todo.service.exception.ApplicationException;
import com.project.todo.service.mapper.ToDoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToDoServiceTest {

    private static final long ID = 1L;

    @Mock
    private ToDoRepository toDoRepository;
    @Mock
    private ToDoMapper toDoMapper;
    @Mock
    private UserService userService;
    @InjectMocks
    private ToDoService toDoService;

    private User user;
    private ToDo toDo;
    private ToDoDto toDoDto;
    private CreateToDoDto createToDoDto;
    private UpdateToDoDto updateToDoDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(ID);

        toDo = new ToDo();
        toDo.setId(ID);
        toDo.setUser(user);
        user.setToDos(List.of(toDo));

        toDoDto = new ToDoDto();
        toDoDto.setId(ID);
        toDoDto.setUserId(ID);

        createToDoDto = new CreateToDoDto();
        updateToDoDto = new UpdateToDoDto();
    }

    @Test
    void getAllToDosForCurrentUser() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(toDoMapper.toDto(toDo)).thenReturn(toDoDto);

        List<ToDoDto> result = toDoService.getAllToDosForCurrentUser();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(toDoDto, result.getFirst());
    }

    @Test
    void getToDoById() {
        when(toDoRepository.findById(ID)).thenReturn(Optional.of(toDo));
        when(toDoMapper.toDto(toDo)).thenReturn(toDoDto);

        ToDoDto result = toDoService.getToDoById(ID);

        assertNotNull(result);
        assertEquals(toDoDto, result);
    }

    @Test
    void getToDoById_notFound() {
        when(toDoRepository.findById(ID)).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> toDoService.getToDoById(ID));

        assertEquals("ToDo not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void createToDo() {
        when(toDoMapper.toEntity(createToDoDto)).thenReturn(toDo);
        when(userService.getCurrentUser()).thenReturn(user);
        when(toDoRepository.save(toDo)).thenReturn(toDo);
        when(toDoMapper.toDto(toDo)).thenReturn(toDoDto);

        ToDoDto result = toDoService.createToDo(createToDoDto);

        assertNotNull(result);
        assertEquals(toDoDto, result);
    }

    @Test
    void updateToDo() {
        when(toDoRepository.findById(ID)).thenReturn(Optional.of(toDo));
        when(toDoRepository.save(toDo)).thenReturn(toDo);
        when(toDoMapper.toDto(toDo)).thenReturn(toDoDto);

        ToDoDto result = toDoService.updateToDo(ID, updateToDoDto);

        assertNotNull(result);
        assertEquals(toDoDto, result);
    }

    @Test
    void updateToDo_checkmarkTrue() {
        ArgumentCaptor<ToDo> toDoCaptor = ArgumentCaptor.forClass(ToDo.class);
        when(toDoRepository.findById(ID)).thenReturn(Optional.of(toDo));
        when(toDoRepository.save(toDo)).thenReturn(toDo);
        when(toDoMapper.toDto(toDo)).thenReturn(toDoDto);
        updateToDoDto.setCheckMark(true);

        ToDoDto result = toDoService.updateToDo(ID, updateToDoDto);

        assertNotNull(result);
        assertEquals(toDoDto, result);

        verify(toDoRepository).save(toDoCaptor.capture());
        assertNotNull(toDoCaptor.getValue().getCompletionDate());
    }

    @Test
    void updateToDo_checkmarkFalse() {
        ArgumentCaptor<ToDo> toDoCaptor = ArgumentCaptor.forClass(ToDo.class);
        when(toDoRepository.findById(ID)).thenReturn(Optional.of(toDo));
        when(toDoRepository.save(toDo)).thenReturn(toDo);
        when(toDoMapper.toDto(toDo)).thenReturn(toDoDto);
        toDo.setCheckMark(true);
        toDo.setCompletionDate(LocalDateTime.now());
        updateToDoDto.setCheckMark(false);

        ToDoDto result = toDoService.updateToDo(ID, updateToDoDto);

        assertNotNull(result);
        assertEquals(toDoDto, result);

        verify(toDoRepository).save(toDoCaptor.capture());
        assertNull(toDoCaptor.getValue().getCompletionDate());
    }

    @Test
    void updateToDo_NotFound() {
        when(toDoRepository.findById(ID)).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class, () -> toDoService.updateToDo(ID, updateToDoDto));

        assertEquals("ToDo not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void deleteToDo() {
        when(toDoRepository.existsById(ID)).thenReturn(true);

        toDoService.deleteToDo(ID);

        verify(toDoRepository, times(1)).deleteById(ID);
    }

    @Test
    void deleteToDo_NotFound() {
        when(toDoRepository.existsById(ID)).thenReturn(false);

        ApplicationException exception = assertThrows(ApplicationException.class, () -> toDoService.deleteToDo(ID));

        assertEquals("ToDo not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }
}