package ru.practicum.util.validation;

public class SizeValidator {
    public static void validateSize(Integer size) {
        if (size == 0) {
            throw new IllegalArgumentException("Размер не может быть меньше 0");
        }
    }
}