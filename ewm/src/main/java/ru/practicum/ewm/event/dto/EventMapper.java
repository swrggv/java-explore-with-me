package ru.practicum.ewm.event.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static EventFullDto toFullEventDtoFromEvent(Event event, @Nullable Integer views) {
        EventFullDto dto = new EventFullDto();
        dto.setId(event.getId());
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(CategoryMapper.toCategoryDtoFromCategory(event.getCategory()));
        dto.setEventDate(event.getEventDate());
        dto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        dto.setLocation(event.getLocation());
        dto.setPaid(event.isPaid());
        dto.setConfirmedRequests(event.getParticipants().size());
        dto.setCreatedOn(event.getCreatedOn());
        dto.setDescription(event.getDescription());
        dto.setParticipantLimit(event.getParticipantLimit());
        dto.setPublishedOn(event.getPublishedOn());
        dto.setState(event.getState());
        dto.setTitle(event.getTitle());
        dto.setViews(views);
        dto.setRequestModeration(event.getRequestModeration());
        return dto;
    }

    public static EventShortDto toShortEventDtoFromEvent(Event event, int views) {
        EventShortDto shortDto = new EventShortDto();
        shortDto.setId(event.getId());
        shortDto.setEventDate(event.getEventDate());
        shortDto.setAnnotation(event.getAnnotation());
        shortDto.setCategory(CategoryMapper.toCategoryDtoFromCategory(event.getCategory()));
        shortDto.setPaid(event.isPaid());
        shortDto.setInitiator(UserMapper.toUserShortDto(event.getInitiator()));
        shortDto.setConfirmedRequests(event.getParticipants().size());
        shortDto.setTitle(event.getTitle());
        shortDto.setViews(views);
        return shortDto;
    }

    public static Event toEventFromNewEventDto(NewEventDto newEventDto, Category category, User user) {
        Event event = new Event();
        event.setTitle(newEventDto.getTitle());
        event.setAnnotation(newEventDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setLocation(newEventDto.getLocation());
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setInitiator(user);
        event.setRequestModeration(newEventDto.getRequestModeration());
        return event;
    }
}
