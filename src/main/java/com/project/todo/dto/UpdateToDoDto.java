package com.project.todo.dto;

import lombok.Data;

@Data
public class UpdateToDoDto extends CreateToDoDto {
    private Boolean checkMark;
}

