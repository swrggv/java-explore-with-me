package ru.practicum.ewm.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.stats.stats.model.EndpointHit;
import ru.practicum.stats.stats.model.ViewStats;

import java.util.List;

@FeignClient(value = "stats", url = "${feign.url}")
public interface Client {
    @RequestMapping(method = RequestMethod.POST, value = "/hit")
    void hit(@RequestBody EndpointHit endpointHit);

    @RequestMapping(method = RequestMethod.GET, value = "/stats")
    List<ViewStats> stats(@RequestParam String start,
                          @RequestParam String end,
                          @RequestParam(required = false) List<String> uris,
                          @RequestParam(defaultValue = "false") Boolean unique);
}
