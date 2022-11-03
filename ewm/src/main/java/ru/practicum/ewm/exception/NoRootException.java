package ru.practicum.ewm.exception;

import lombok.Getter;

@Getter
public class NoRootException extends RuntimeException {
    private final String reason;

    public NoRootException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
}
