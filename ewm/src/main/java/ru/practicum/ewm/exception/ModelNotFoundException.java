package ru.practicum.ewm.exception;

import lombok.Getter;

@Getter
public class ModelNotFoundException extends RuntimeException {
    private final String reason;

    public ModelNotFoundException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
}
