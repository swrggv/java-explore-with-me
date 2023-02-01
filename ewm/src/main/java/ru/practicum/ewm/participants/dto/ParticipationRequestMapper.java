package ru.practicum.ewm.participants.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.participants.model.ParticipationRequest;

import java.util.List;

@Mapper
public interface ParticipationRequestMapper {
    @Mapping(target = "event", source = "request.event.id")
    @Mapping(target = "requestor", source = "request.requestor.id")
    ParticipationRequestDto toDto(ParticipationRequest request);
    List<ParticipationRequestDto> toListDto(List<ParticipationRequest> requests);
}
