package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.Create;
import ru.practicum.ewm.event.model.Location;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    //@Max(groups = Create.class, value = 2000, message = "Annotation can not be more then 2000 characters")
    //@Min(groups = Create.class, value = 20, message = "Annotation can not be less then 20 characters")
    private String annotation;
    private Integer category; //id категории
    //@Max(groups = Create.class, value = 7000, message = "Description can not be more then 7000 characters")
    //@Min(groups = Create.class, value = 20, message = "Description can not be less then 20 characters")
    private String description;
    @Future(groups = Create.class, message = "EventDate can not be in the past")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    //@Max(groups = Create.class, value = 2000, message = "Title can not be more then 2000 characters")
    //@Min(groups = Create.class, value = 20, message = "Title can not be less then 20 characters")
    private String title;
}
