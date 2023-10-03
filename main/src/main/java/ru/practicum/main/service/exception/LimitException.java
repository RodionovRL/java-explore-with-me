package ru.practicum.main.service.exception;

public class LimitException extends RuntimeException {
    public LimitException(String message) {
        super(message);
    }
}
