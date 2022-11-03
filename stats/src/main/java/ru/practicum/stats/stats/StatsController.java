package ru.practicum.stats.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.stats.model.EndpointHit;
import ru.practicum.stats.stats.model.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Validated
@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public void hit(@RequestBody EndpointHit endpointHit) {
        log.info("Hit {}", endpointHit);
        statsService.hit(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> stats(@RequestParam String start,
                                 @RequestParam String end,
                                 @RequestParam(required = false) List<String> uris,
                                 @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Getting statistic");
        return statsService.getStatistic(LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                uris, unique);
    }
}
