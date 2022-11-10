package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.Create;
import ru.practicum.ewm.event.model.Location;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank(groups = Create.class, message = "Annotation can not be empty")
    private String annotation;
    @NotBlank
    private Integer category;
    @NotBlank(groups = Create.class, message = "Description can not be empty")
    private String description;
    @Future(groups = Create.class, message = "EventDate can not be in the past")
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotBlank(groups = Create.class, message = "Title can not be empty")
    private String title;
}
