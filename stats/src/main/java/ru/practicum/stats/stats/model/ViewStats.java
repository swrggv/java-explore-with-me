package ru.practicum.stats.stats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewStats {
    String app;
    String uri;
    int hits;
}
