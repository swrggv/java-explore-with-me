package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.ClientService;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Validated
@RequestMapping("/events")
@Slf4j
@RequiredArgsConstructor
public class EventPublicController {
    private final EventService eventService;
    private final ClientService clientService;

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Integer> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(required = false) String sort,
                                         @Valid @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                         @Valid @Positive @RequestParam(defaultValue = "10") int size,
                                         HttpServletRequest request) {
        log.info("Public get list events with parameters");
        clientService.hit(request.getRemoteAddr(), request.getRequestURI());
        return eventService.getEventsPublic(text, categories, paid,
                LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                onlyAvailable, sort,
                from, size);
    }

    @GetMapping("{id}")
    public EventFullDto getEventById(@PathVariable long id, HttpServletRequest request) {
        log.info("Public get full event {}", id);
        clientService.hit(request.getRemoteAddr(), request.getRequestURI());
        return eventService.getEventByIdPublic(id);
    }
}
