package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.model.ErrorResponse;
import ru.practicum.shareit.exception.model.NotFoundException;
import ru.practicum.shareit.exception.model.ConflictException;
import ru.practicum.shareit.exception.model.SecurityException;
import ru.practicum.shareit.exception.model.ValidationException;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(NotFoundException e) {
        log.info(("Exception! Not found entity"));
        return new ErrorResponse("Invalid ID.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(ValidationException e) {
        log.info(("Exception! Entity is not validated"));
        return new ErrorResponse("Validation error.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorResponse handle(ConflictException e) {
        log.info("Exception! Conflict data");
        return new ErrorResponse("Data called conflict", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorResponse handle(SecurityException e) {
        log.info("Exception! Access error");
        return new ErrorResponse("Access error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(Throwable e) {
        log.info("Exception! Unknown error");
        return new ErrorResponse("Unknown error", Arrays.toString(e.getStackTrace()));
    }

}