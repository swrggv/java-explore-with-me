package ru.practicum.stats.stats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Autowired
    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Override
    @Transactional
    public void hit(EndpointHit endpointHit) {
        statsRepository.save(endpointHit);
    }

    @Override
    public List<ViewStats> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<ViewStats> viewStats = new ArrayList<>();
        List<EndpointHit> hits;

        for (String uri : uris) {
            if (unique) {
                hits = statsRepository.findDistinctByUriInAndTimestampBetween(List.of(uri), start, end);
            } else {
                hits = statsRepository.findByUriInAndTimestampBetween(List.of(uri), start, end);
            }
            viewStats.add(new ViewStats("ewm-main-service", uri, hits.size()));
        }
        return viewStats;
    }
}
