package ru.practicum.ewm.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.endpointHit.EndpointHit;
import ru.practicum.ewm.endpointHit.ViewStats;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {
    private final Client client;

    @Autowired
    public ClientServiceImpl(Client client) {
        this.client = client;
    }

    @Override
    public void hit(String ip, String uri) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setIp(ip);
        endpointHit.setApp("ewm-main-service");
        endpointHit.setTimestamp(LocalDateTime.now());
        endpointHit.setUri(uri);
        client.hit(endpointHit);
        log.info("Request was sent from ip {} and uri {}", ip, uri);
    }

    @Override
    public int getStats(String uri) {
        String start = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = LocalDateTime.now().plusYears(1000).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        List<ViewStats> views = client.stats(start, end, List.of(uri), false);
        log.info("Get statistic {}", uri);
        if (views != null && views.size() > 0) {
            return views.get(0).getHits();
        } else {
            return 0;
        }
    }
}
