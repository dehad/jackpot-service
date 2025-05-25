package com.example.jackpot.controller;

import com.example.jackpot.dto.ErrorDto;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.boot.context.properties.bind.validation.BindValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleNotValidProperties(final MethodArgumentNotValidException e,
                                            final HttpServletRequest r) {
        return new ErrorDto(e.getFieldErrors().stream().map(
                error -> String.format("Field '%s' %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList()));
    }
    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleBindErrors(final InvalidFormatException e,
                                     final HttpServletRequest r) {
        return new ErrorDto(List.of(String.format("Field '%s' %s",
                e.getPath().get(0).getFieldName(), e.getOriginalMessage())));
    }


}
