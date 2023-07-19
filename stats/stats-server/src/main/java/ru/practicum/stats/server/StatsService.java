package ru.practicum.stats.server;

import java.util.List;

public interface StatsService {
    EndpointHit add(EndpointHit endpointHitDto);

    List<ViewStats> getStats(String start, String end, List<String> uri, Boolean unique);
}
