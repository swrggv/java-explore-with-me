package ru.practicum.ewm.participants;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.participants.model.ParticipationRequest;
import ru.practicum.ewm.user.Status;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByEventAndStatus(Event event, Status status);

    @Query(value = "select exists(select * from participation_requests as p " +
            "where p.id_requestor = :userId and p.id_event = :eventId)", nativeQuery = true)
    boolean isExists(@Param("userId") long userId, @Param("eventId") long eventId);

    List<ParticipationRequest> findAllByEvent(Event event);

    int countParticipationRequestByEventAndStatus(Event event, Status status);

    List<ParticipationRequest> findAllByRequestor(User user);
}
