package ru.practicum.ewm.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
    private final String reason;

    public BadRequestException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
}
