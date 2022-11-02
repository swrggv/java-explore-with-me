package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.event.dto.EventFullDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Validated
@RequestMapping("/admin/events")
@Slf4j
@RequiredArgsConstructor
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getFullEvents(@RequestParam(required = false) List<Long> users,
                                            @RequestParam(required = false) List<State> states,
                                            @RequestParam(required = false) List<Integer> categories,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                            @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                            @Valid @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Get event by parameters");
        return eventService.getEventAdmin(users, states, categories, rangeStart, rangeEnd,
                from, size);
    }

    @PutMapping("{eventId}")
    public EventFullDto changeEvent(@RequestBody AdminUpdateEventRequest adminEvent,
                                    @PathVariable long eventId) {
        log.info("Change event {}", eventId);
        return eventService.changeEventAdmin(eventId, adminEvent);
    }

    @PatchMapping("{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable long eventId) {
        log.info("Publish event {}", eventId);
        return eventService.publishEventAdmin(eventId);
    }

    @PatchMapping("{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable long eventId) {
        log.info("Reject event {}", eventId);
        return eventService.rejectEvent(eventId);
    }
}
