package ru.practicum.ewm.exception;

import lombok.Getter;

@Getter
public class ModelAlreadyExistException extends RuntimeException {
    private final String reason;

    public ModelAlreadyExistException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
}
