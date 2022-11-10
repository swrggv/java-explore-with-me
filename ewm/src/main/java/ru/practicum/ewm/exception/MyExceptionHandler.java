package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ModelAlreadyExistException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(Exception ex) {
        log.warn(ex.getMessage());
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .reason("Validation Exception")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ModelNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleModelNotFoundException(ModelNotFoundException ex) {
        log.warn(ex.getMessage());
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .reason(ex.getReason())
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn(ex.getMessage());
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .reason(ex.getCause().toString())
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(NoRootException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleConstraintViolationException(NoRootException ex) {
        log.warn(ex.getMessage());
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .reason(ex.getReason())
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException ex) {
        log.warn(ex.getMessage());
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .reason(ex.getReason())
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllExceptions(Throwable ex) {
        log.warn(ex.getMessage());
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .reason(ex.getCause().toString())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
