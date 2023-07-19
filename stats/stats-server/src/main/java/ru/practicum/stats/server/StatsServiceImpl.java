package ru.practicum.stats.server;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final StatsRepository statsRepository;

    @Transactional
    @Override
    public EndpointHit add(EndpointHit endpointHit) {
        endpointHit.setTimestamp(LocalDateTime.now());
        EndpointHit createdEndpointHit = statsRepository.save(endpointHit);
        return createdEndpointHit;
    }

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique) {
        if (unique == null) {
            unique = Boolean.FALSE;
        }
        LocalDateTime startDate = LocalDateTime.parse(start, DATE_TIME_FORMATTER);
        LocalDateTime endDate = LocalDateTime.parse(end, DATE_TIME_FORMATTER);
        if (unique) {
            if (uris == null || uris.isEmpty()) {
                return statsRepository.searchByParamsWithUnique(startDate, endDate);
            } else {
                return statsRepository.searchByParamsWithUnique(startDate, endDate, uris);
            }
        }
        if (uris == null || uris.isEmpty()) {
            return statsRepository.searchByParamsWithoutUnique(startDate, endDate);
        } else {
            return statsRepository.searchByParamsWithoutUnique(startDate, endDate, uris);
        }
    }
}
