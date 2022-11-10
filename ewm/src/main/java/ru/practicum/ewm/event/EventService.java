package ru.practicum.ewm.event;

import ru.practicum.ewm.event.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto publishEventAdmin(long eventId);

    EventFullDto rejectEvent(long eventId);

    EventFullDto changeEventAdmin(long eventId, AdminUpdateEventRequest adminEvent);

    List<EventFullDto> getEventAdmin(List<Long> users,
                                     List<State> states,
                                     List<Integer> categories,
                                     LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd,
                                     int from, int size);

    List<EventShortDto> getEventsPublic(String text,
                                        List<Integer> categories,
                                        Boolean paid,
                                        String rangeStart,
                                        String rangeEnd,
                                        Boolean onlyAvailable,
                                        String sort,
                                        int from,
                                        int size);

    EventFullDto getEventByIdPublic(long id);

    List<EventShortDto> getAllEventsPrivate(Long userId, int from, int size);

    EventFullDto patchEventPrivate(Long userId, UpdateEventRequest updateEvent);

    EventFullDto addEventPrivate(Long userId, NewEventDto newEventDto);

    EventFullDto getEventById(Long eventId);

    List<EventFullDto> getEventsByIds(List<Long> events);
}
