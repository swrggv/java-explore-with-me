package ru.practicum.stats.stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void hit(EndpointHit endpointHit);

    List<ViewStats> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
