package ru.practicum.stats.client;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stats.dto.EndpointHitDtoRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class StatsClient extends BaseClient {

    public StatsClient(String statsServerUrl) {
        super(
                new RestTemplateBuilder()
                        .uriTemplateHandler(new DefaultUriBuilderFactory(statsServerUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> sendStat(EndpointHitDtoRequest dto) {
        ResponseEntity<Object> post = post("/hit", dto);
        return post;
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uri, Boolean unique) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        parameters.put("end", end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        parameters.put("uri", uri);
        parameters.put("unique", unique);

        return get("/stats", parameters);
    }
}
