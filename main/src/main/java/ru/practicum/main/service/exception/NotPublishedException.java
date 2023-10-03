package ru.practicum.main.service.exception;

public class NotPublishedException extends RuntimeException {
    public NotPublishedException(String message) {
        super(message);
    }
}
