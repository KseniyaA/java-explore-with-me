package ru.practicum.stats.server;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    EndpointHit add(EndpointHit endpointHitDto);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uri, Boolean unique);
}
