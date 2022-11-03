package ru.practicum.ewm.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ErrorResponse {
    private List<String> errors;
    private String message;
    private String reason;
    private String status;
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime timestamp;
}
