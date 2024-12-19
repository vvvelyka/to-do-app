package com.project.todo.service.mapper;

import com.project.todo.domain.ToDo;
import com.project.todo.dto.CreateToDoDto;
import com.project.todo.dto.ToDoDto;
import com.project.todo.dto.UpdateToDoDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ToDoMapper {

    @Mapping(target = "userId", source = "user.id")
    ToDoDto toDto(ToDo toDo);

    ToDo toEntity(CreateToDoDto createToDoDto);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget ToDo toDo, UpdateToDoDto updateToDoDto);

}
