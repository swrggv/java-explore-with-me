package ru.practicum.ewm.participants;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.State;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.ModelNotFoundException;
import ru.practicum.ewm.exception.NoRootException;
import ru.practicum.ewm.participants.dto.ParticipationRequestDto;
import ru.practicum.ewm.participants.dto.ParticipationRequestMapper;
import ru.practicum.ewm.participants.model.ParticipationRequest;
import ru.practicum.ewm.user.Status;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.user.model.User;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {
    private final ParticipationRepository participationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ParticipationRequestDto addRequest(long userId, long eventId) {
        User requestor = fromOptionalToUser(userId);
        Event event = fromOptionalToEvent(eventId);
        checkRequest(event, userId);
        ParticipationRequest request = new ParticipationRequest(event, requestor);
        int limit = event.getParticipantLimit();
        int confirmedRequests = event.getConfirmedRequests();
        if ((!event.getRequestModeration()) && (limit == 0 || confirmedRequests < limit)) {
            request.setStatus(Status.CONFIRMED);
        }
        request = participationRepository.save(request);
        return ParticipationRequestMapper.toDtoFromRequest(request);
    }

    @Override
    @Transactional
    public ParticipationRequestDto rejectRequestFromOwnerRequest(long userId, long requestId) {
        ParticipationRequest request = fromOptionalToRequest(requestId);
        Event event = request.getEvent();
        if (request.getRequestor().getId() == userId) {
            if (request.getStatus() == Status.CONFIRMED) {
                event.setConfirmedRequests(event.getConfirmedRequests() - 1);
                eventRepository.save(event);
            }
            request.setStatus(Status.CANCELED);
            request = participationRepository.save(request);
        } else {
            throw new NoRootException("Could not reject request",
                    String.format("User %s is not requestor for request %d", userId, requestId));
        }
        return ParticipationRequestMapper.toDtoFromRequest(request);
    }

    @Override
    public List<ParticipationRequestDto> getAllRequestsForUser(long userId) {
        User user = fromOptionalToUser(userId);
        List<ParticipationRequest> requests = participationRepository.findAllByRequestor(user);
        return ParticipationRequestMapper.toListDtoFromRequests(requests);
    }

    @Override
    public List<ParticipationRequestDto> getRequestForOwner(long eventId, long userId) {
        Event event = fromOptionalToEvent(eventId);
        if (event.getInitiator().getId() == userId) {
            return ParticipationRequestMapper.toListDtoFromRequests(participationRepository.findAllByEvent(event));
        } else {
            throw new NoRootException("No root for this action",
                    String.format("User %d is not owner the event %d", userId, eventId));
        }
    }

    @Override
    @Transactional
    public ParticipationRequestDto confirmRequest(long eventId, long reqId, long userId) {
        Event event = fromOptionalToEvent(eventId);
        ParticipationRequest request = fromOptionalToRequest(reqId);
        if (event.getConfirmedRequests() < event.getParticipantLimit() || event.getParticipantLimit() == 0) {
            request.setStatus(Status.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            participationRepository.save(request);
            eventRepository.save(event);
        } else {
            rejectAll(event);
            throw new BadRequestException("Limit is over",
                    String.format("For event %d participation limit is over", eventId));
        }
        return ParticipationRequestMapper.toDtoFromRequest(request);
    }

    @Override
    @Transactional
    public ParticipationRequestDto rejectRequestFromOwnerEvent(long userId, long eventId, long reqId) {
        ParticipationRequest request = fromOptionalToRequest(reqId);
        Event event = fromOptionalToEvent(eventId);
        if (event.getInitiator().getId() == userId) {
            request.setStatus(Status.REJECTED);
        } else {
            throw new NoRootException("Impossible to reject request",
                    String.format("USer %d is not owner the event %d", userId, eventId));
        }
        int confirmedRequests = participationRepository.countParticipationRequestByEventAndStatus(event, Status.CONFIRMED);
        event.setConfirmedRequests(confirmedRequests);
        eventRepository.save(event);
        participationRepository.save(request);
        return ParticipationRequestMapper.toDtoFromRequest(request);
    }

    private void rejectAll(Event event) {
        event.getParticipants().stream()
                .filter(x -> x.getStatus() == Status.PENDING)
                .forEach(x -> x.setStatus(Status.REJECTED));

    }

    private User fromOptionalToUser(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ModelNotFoundException("Could not get user",
                        String.format("User %d not found", userId)));
    }

    private Event fromOptionalToEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new ModelNotFoundException("Could not get event",
                        String.format("Event %d not found", eventId)));
    }

    private ParticipationRequest fromOptionalToRequest(long requestId) {
        return participationRepository.findById(requestId).orElseThrow(
                () -> new ModelNotFoundException("Could not get request",
                        String.format("Request %d not found", requestId)));
    }

    private void checkRequest(Event event, long userId) {
        long eventId = event.getId();
        if (event.getInitiator().getId() == userId) {
            throw new BadRequestException("Impossible to add request",
                    String.format("User %d is initiator of event %d", userId, eventId));
        }
        if (event.getState() != State.PUBLISHED) {
            throw new BadRequestException("Impossible to add request",
                    String.format("Event is not publish %d", eventId));
        }
        if (isExistRequest(userId, eventId)) {
            throw new BadRequestException("Impossible to add request", "Impossible create second request");
        }
        if (event.getParticipants().size() >= event.getParticipantLimit()) {
            throw new BadRequestException("Impossible to add request",
                    String.format("Limit participants has been reached for event %d", eventId));
        }
    }

    private boolean isExistRequest(long userId, long eventId) {
        return participationRepository.isExists(userId, eventId);
    }
}
