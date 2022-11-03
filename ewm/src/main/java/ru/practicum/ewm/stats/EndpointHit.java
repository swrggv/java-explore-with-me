package ru.practicum.ewm.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHit {
    long id;
    String app;
    String uri;
    String ip;
    LocalDateTime timestamp;
}
