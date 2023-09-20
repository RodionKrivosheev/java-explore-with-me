package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(BAD_REQUEST)
    public ApiModelError handleValidationException(MethodArgumentNotValidException e) {
        log.debug(e.toString());
        return ApiModelError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .status(BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }


    @ExceptionHandler({NonTransientDataAccessException.class})
    @ResponseStatus(CONFLICT)
    public ApiModelError handleNonTransientDataAccessException(final NonTransientDataAccessException e) {
        log.debug(e.toString());
        return ApiModelError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getCause().getMessage())
                .reason(NestedExceptionUtils.getMostSpecificCause(e).getMessage())
                .status(CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }


    @ExceptionHandler({ServerWebInputException.class})
    @ResponseStatus(BAD_REQUEST)
    public ApiModelError handleServerWebInputException(final ServerWebInputException e) {
        log.debug(e.toString());
        return ApiModelError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .message(e.getMessage())
                .reason(e.getReason())
                .status(BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiModelError handleValidationException(ValidationException e) {
        return ApiModelError.builder()
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.toList()))
                .status(BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
