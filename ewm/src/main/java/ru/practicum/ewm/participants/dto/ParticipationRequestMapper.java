package ru.practicum.ewm.participants.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.participants.model.ParticipationRequest;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParticipationRequestMapper {
    public static ParticipationRequestDto toDtoFromRequest(ParticipationRequest request) {
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setId(request.getId());
        dto.setEvent(request.getEvent().getId());
        dto.setRequester(request.getRequestor().getId());
        dto.setCreated(request.getCreated());
        dto.setStatus(request.getStatus());
        return dto;
    }

    public static List<ParticipationRequestDto> toListDtoFromRequests(List<ParticipationRequest> requests) {
        return requests.stream()
                .map(ParticipationRequestMapper::toDtoFromRequest)
                .collect(Collectors.toList());
    }
}
