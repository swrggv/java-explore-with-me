package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.Update;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequest {
    @NotNull(groups = Update.class, message = "Id event is required")
    private Long eventId;
    private String annotation;
    private Integer category;
    private String description;
    private LocalDateTime eventDate;
    private Boolean paid;
    private Integer participantLimit;
    private String title;
}
