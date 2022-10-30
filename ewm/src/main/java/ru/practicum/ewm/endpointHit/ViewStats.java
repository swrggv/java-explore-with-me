package ru.practicum.ewm.endpointHit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewStats {
    String app;
    URI uri;
    int hits;
}
