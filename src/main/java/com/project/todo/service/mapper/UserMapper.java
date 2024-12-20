package com.project.todo.service.mapper;

import com.project.todo.domain.User;
import com.project.todo.dto.UserRegistrationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRegistrationDto userRegistrationDto);

}