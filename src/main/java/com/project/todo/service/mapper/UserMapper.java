package com.project.todo.service.mapper;

import com.project.todo.domain.User;
import com.project.todo.dto.UserDto;
import com.project.todo.dto.UserRegistrationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserRegistrationDto userRegistrationDto);

}