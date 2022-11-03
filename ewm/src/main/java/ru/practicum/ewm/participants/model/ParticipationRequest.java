package ru.practicum.ewm.participants.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.Status;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "participation_requests")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_participation_request")
    private long id;
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    @Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();
    @ManyToOne
    @JoinColumn(name = "id_event", referencedColumnName = "id_event")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "id_requestor", referencedColumnName = "id_user")
    private User requestor;
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    public ParticipationRequest(Event event, User requestor) {
        this.event = event;
        this.requestor = requestor;
    }
}
