package ru.practicum.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String object, Long id) {
        super(String.format("%s with id=%d was not found.", object, id));
    }

    public NotFoundException(String message) {
        super(message);
    }
}
