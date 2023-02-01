package ru.practicum.ewm.event.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.lang.Nullable;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

@Mapper
public interface EventMapper {
    @Mapping(target = "views", defaultValue = "0")
    EventFullDto toFullEventDto(Event event, @Nullable Integer views);

    EventShortDto toShortEventDto(Event event, int views);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "category")
    @Mapping(target = "initiator", source = "user")
    Event toEvent(NewEventDto newEventDto, Category category, User user);
}
