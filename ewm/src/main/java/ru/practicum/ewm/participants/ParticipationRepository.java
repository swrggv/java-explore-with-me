package ru.practicum.ewm.participants;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.participants.model.ParticipationRequest;
import ru.practicum.ewm.user.Status;
import ru.practicum.ewm.user.model.User;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<ParticipationRequest, Long> {
    @Query(value = "select * from participation_requests as p " +
            "where p.id_event = :eventId and status = :status", nativeQuery = true)
    List<ParticipationRequest> findAllParticipantsRequest(@Param("eventId") long eventId,
                                                          @Param("status") String status);

    @Query(value = "select exists(select * from participation_requests as p " +
            "where p.id_requestor = :userId and p.id_event = :eventId)", nativeQuery = true)
    boolean isExists(@Param("userId") long userId, @Param("eventId") long eventId);

    List<ParticipationRequest> findAllByEvent(Event event);

    int countParticipationRequestByEventAndStatus(Event event, Status status);

    List<ParticipationRequest> findAllByRequestor(User user);
}
