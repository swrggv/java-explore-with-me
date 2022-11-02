package ru.practicum.stats.stats;

import ru.practicum.stats.stats.model.EndpointHit;
import ru.practicum.stats.stats.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void hit(EndpointHit endpointHit);

    List<ViewStats> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
