package ru.practicum.stats.client;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.EndpointHitMapper;
import ru.practicum.stats.server.EndpointHit;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
public class StatsClient extends BaseClient {
    private static final String STATS_SERVICE_URL = "http://stats-service-container:9090";

    public StatsClient() {
        super(
                new RestTemplateBuilder()
                        .uriTemplateHandler(new DefaultUriBuilderFactory(STATS_SERVICE_URL))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> sendStat(EndpointHitDto dto) {
        EndpointHit endHit = EndpointHitMapper.toEndpointHit(dto);
        ResponseEntity<Object> post = post("/hit", endHit);
        System.out.println(post);
        return post;
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uri, Boolean unique) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start);
        parameters.put("end", end);
        parameters.put("uri", uri);
        parameters.put("unique", unique);

        return get("/stats", parameters);
    }
}
