package ru.practicum.ewm.participants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.participants.dto.ParticipationRequestDto;

import java.util.List;

@Validated
@RestController
@Slf4j
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class ParticipationPrivateController {
    private final ParticipationService participationService;

    @GetMapping
    public List<ParticipationRequestDto> getRequests(@PathVariable long userId) {
        log.info("Get all user's {} requests", userId);
        return participationService.getAllRequestsForUser(userId);
    }

    @PostMapping
    public ParticipationRequestDto addRequest(@PathVariable long userId,
                                              @RequestParam long eventId) {
        log.info("Add participation request from user {} to event {}", userId, eventId);
        return participationService.addRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto rejectRequest(@PathVariable long userId,
                                                 @PathVariable long requestId) {
        log.info("Reject user's request {}", requestId);
        return participationService.rejectRequestFromOwnerRequest(userId, requestId);
    }
}
