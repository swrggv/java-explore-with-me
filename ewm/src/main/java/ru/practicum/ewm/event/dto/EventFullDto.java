package ru.practicum.ewm.event.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.event.State;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private State state;
    private String title;
    private Integer views;
    private Boolean requestModeration;
    private List<CommentDto> comments = new ArrayList<>();
}
