package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.Create;
import ru.practicum.ewm.Update;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.participants.ParticipationService;
import ru.practicum.ewm.participants.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Validated
@RequestMapping("/users/{userId}/events")
@Slf4j
@RequiredArgsConstructor
public class EventPrivateController {
    private final EventService eventService;

    private final ParticipationService participationService;

    @GetMapping
    public List<EventShortDto> getAllUserEvents(@PathVariable Long userId,
                                                @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Get all events for user {}", userId);
        return eventService.getAllEventsPrivate(userId, from, size);
    }

    @PatchMapping
    public EventFullDto patchEventPrivate(@PathVariable Long userId,
                                          @RequestBody @Validated(Update.class) UpdateEventRequest updateEvent) {
        log.info("Patch event {} by user {}", updateEvent, userId);
        return eventService.patchEventPrivate(userId, updateEvent);
    }

    @PostMapping
    public EventFullDto postEventPrivate(@PathVariable Long userId,
                                         @RequestBody @Validated(Create.class) NewEventDto newEventDto) {
        log.info("Create event {} by user {}", newEventDto, userId);
        return eventService.addEventPrivate(userId, newEventDto);
    }

    @GetMapping("{eventId}")
    public EventFullDto getEvent(@PathVariable long eventId, @PathVariable long userId) {
        log.info("Get full event {} for owner {}", eventId, userId);
        return eventService.getEventById(eventId);
    }

    @PatchMapping("{eventId}")
    public EventFullDto canselEvent(@PathVariable long eventId, @PathVariable long userId) {
        log.info("Cansel event {} by owner user {}", eventId, userId);
        return eventService.rejectEvent(eventId);
    }

    @GetMapping("{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequests(@PathVariable long eventId, @PathVariable long userId) {
        log.info("Get all participation requests for event {}", eventId);
        return participationService.getRequestForOwner(eventId, userId);
    }

    @PatchMapping("{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmParticipationRequest(@PathVariable long eventId,
                                                               @PathVariable long reqId,
                                                               @PathVariable long userId) {
        log.info("Confirm participation request {}", reqId);
        return participationService.confirmRequest(eventId, reqId, userId);
    }

    @PatchMapping("{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectParticipationRequest(@PathVariable long userId,
                                                              @PathVariable long eventId,
                                                              @PathVariable long reqId) {
        log.info("Reject participant request {} from user {} on event {}", reqId, userId, eventId);
        return participationService.rejectRequestFromOwnerEvent(userId, eventId, reqId);
    }
}
