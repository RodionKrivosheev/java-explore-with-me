package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.CONFLICT;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    // 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiModelError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.debug(e.toString());
        return ApiModelError.builder()
                .errors(Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.toList()))
                .status(BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ServerWebInputException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiModelError handleServerWebInputException(final ServerWebInputException e) {
        log.debug(e.toString());
        return ApiModelError.builder()
                .errors(Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.toList()))
                .message(e.getMessage())
                .reason(e.getReason())
                .status(BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiModelError handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.debug(e.toString());
        return ApiModelError.builder()
                .errors(Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.toList()))
                .message(e.getMessage())
                .reason(NestedExceptionUtils.getMostSpecificCause(e).getMessage())
                .status(BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiModelError handleValidationException(ValidationException e) {
        return ApiModelError.builder()
                .errors(Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.toList()))
                .status(BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 404
    @ExceptionHandler({NoSuchElementException.class})
    @ResponseStatus(NOT_FOUND)
    public ApiModelError handleNoSuchElementException(final NoSuchElementException e) {
        return ApiModelError.builder()
                .errors(Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.toList()))
                .status(NOT_FOUND)
                .reason("Object not found")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ApiModelError handleNotFound(final NotFoundException e) {
        log.debug(e.toString());
        return ApiModelError.builder()
                .errors(Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.toList()))
                .status(NOT_FOUND)
                .reason("The required object was not found.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 409
    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ApiModelError handleConflictException(ConflictException e) {
        log.debug(e.toString());
        return ApiModelError.builder()
                .errors(Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.toList()))
                .status(CONFLICT)
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(NonTransientDataAccessException.class)
    @ResponseStatus(CONFLICT)
    public ApiModelError handleNonTransientDataAccessException(final NonTransientDataAccessException e) {
        log.debug(e.toString());
        return ApiModelError.builder()
                .errors(Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.toList()))
                .message(e.getCause().getMessage())
                .reason(NestedExceptionUtils.getMostSpecificCause(e).getMessage())
                .status(CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 500
    @ExceptionHandler({Exception.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiModelError handleException(final Exception e) {
        return ApiModelError.builder()
                .errors(Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.toList()))
                .status(INTERNAL_SERVER_ERROR)
                .reason("The server encountered an unexpected condition that prevented it from fulfilling the request.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
