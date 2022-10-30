package ru.practicum.ewm.event.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.State;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

//возвращается только для авторизованных пользователей
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private LocalDateTime eventDate; //дата и время на которое намечено событие
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer confirmedRequests;
    private LocalDateTime createdOn; //дата и время создания события
    private String description;
    private Integer participantLimit;
    private LocalDateTime publishedOn; //дата и время публикации события
    private State state; //или списком??
    private String title;
    private Integer views;
    private Boolean requestModeration;
}
