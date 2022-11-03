package ru.practicum.ewm.participants;

import ru.practicum.ewm.participants.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationService {
    ParticipationRequestDto addRequest(long userId, long eventId);

    ParticipationRequestDto rejectRequestFromOwnerRequest(long userId, long requestId);

    List<ParticipationRequestDto> getAllRequestsForUser(long userId);

    List<ParticipationRequestDto> getRequestForOwner(long eventId, long userId);

    ParticipationRequestDto confirmRequest(long eventId, long reqId, long userId);

    ParticipationRequestDto rejectRequestFromOwnerEvent(long userId, long eventId, long reqId);
}
