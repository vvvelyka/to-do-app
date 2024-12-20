package com.project.todo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class ApiErrorDto {
    private int status;
    private String message;
    private Map<String, Object> properties;
}
