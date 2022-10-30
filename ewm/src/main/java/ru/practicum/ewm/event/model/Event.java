package ru.practicum.ewm.event.model;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.State;
import ru.practicum.ewm.participants.model.ParticipationRequest;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event")
    private long id;
    @Column(name = "title")
    private String title;
    @Column(name = "annotation")
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "id_category", referencedColumnName = "id_category")
    private Category category;
    @Column(name = "description")
    private String description;
    @Column(name = "event_date")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime eventDate;
    @Column(name = "created_on")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime createdOn = LocalDateTime.now();
    @ManyToOne
    @JoinColumn(name = "id_location", referencedColumnName = "id_location")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Location location;
    @Column(name = "paid")
    private boolean paid;
    @Column(name = "participant_limit")
    private int participantLimit;
    @Column(name = "published_date")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime publishedOn;
    @Enumerated(EnumType.STRING)
    private State state = State.PENDING;
    @ManyToOne
    @JoinColumn(name = "id_initiator", referencedColumnName = "id_user")
    //@Cascade(org.hibernate.annotations.CascadeType.ALL)
    private User initiator;
    @OneToMany
    @JoinColumn(name = "id_participation_request")
    private List<ParticipationRequest> participants = new ArrayList<>();
    @Column(name = "request_moderation")
    private Boolean requestModeration = false;
    @Column(name = "confirmed_requests")
    private int confirmedRequests;
}
