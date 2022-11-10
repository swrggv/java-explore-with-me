package ru.practicum.ewm.comment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comment")
    private long id;
    @Column(name = "text")
    private String text;
    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    private User user;
    @ManyToOne
    @JoinColumn(name = "id_event", referencedColumnName = "id_event")
    private Event event;

    public Comment(String text, User user, Event event) {
        this.text = text;
        this.user = user;
        this.event = event;
    }
}
