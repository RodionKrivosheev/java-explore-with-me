package ru.practicum.exceptions;

public class StartIsAfterEndException extends RuntimeException {

    public StartIsAfterEndException(String message) {
        super(message);
    }
}
