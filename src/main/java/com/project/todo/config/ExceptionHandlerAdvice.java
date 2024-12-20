package com.project.todo.config;

import com.project.todo.dto.ApiErrorDto;
import com.project.todo.service.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerAdvice {

    private static final String ERRORS = "errors";

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDto handleIllegalArgumentException(IllegalArgumentException ex) {
        return mapExceptionToErrorDto(ex).setStatus(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiErrorDto handleAccessDeniedException(AccessDeniedException ex) {
        return mapExceptionToErrorDto(ex).setStatus(HttpStatus.FORBIDDEN.value());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDto handleValidationException(MethodArgumentNotValidException ex) {
        ApiErrorDto errorDto = mapExceptionToErrorDto(ex)
                .setStatus(HttpStatus.BAD_REQUEST.value())
                        .setMessage("Validation failed for one or several fields");
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            errors.add(error.getDefaultMessage());
        });
        errorDto.setProperties(Map.of(ERRORS, errors));
        return errorDto;
    }

    @ExceptionHandler({HttpMessageConversionException.class, MethodArgumentTypeMismatchException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDto handleAccessDeniedException(HttpMessageConversionException ex) {
        return mapExceptionToErrorDto(ex).setStatus(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorDto handleAccessDeniedException(DataAccessException ex) {
        return mapExceptionToErrorDto(ex).setStatus(HttpStatus.BAD_REQUEST.value())
                .setMessage("Database error happened");
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiErrorDto handleAccessDeniedException(AuthorizationDeniedException ex) {
        return mapExceptionToErrorDto(ex).setStatus(HttpStatus.FORBIDDEN.value());
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiErrorDto> handleApplicationException(ApplicationException ex) {
        return ResponseEntity.status(ex.getHttpStatus())
                .body(mapExceptionToErrorDto(ex).setStatus(ex.getHttpStatus().value()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorDto handleAccessDeniedException(Exception ex) {
        return mapExceptionToErrorDto(ex)
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setMessage("Something went wrong");
    }



    private ApiErrorDto mapExceptionToErrorDto(Exception ex) {
        log.error(ex.getMessage());
        return new ApiErrorDto().setMessage(ex.getMessage());
    }
}
