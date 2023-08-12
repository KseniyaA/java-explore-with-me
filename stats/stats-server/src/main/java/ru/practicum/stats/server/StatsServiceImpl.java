package ru.practicum.stats.server;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.err_handler.BadParameterException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Transactional
    @Override
    public EndpointHit add(EndpointHit endpointHit) {
        endpointHit.setTimestamp(LocalDateTime.now());
        return statsRepository.save(endpointHit);
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime startDate, LocalDateTime endDate, List<String> uris, Boolean unique) {
        if (endDate.isBefore(startDate)) {
            throw new BadParameterException("Ошибка входных параметров");
        }
        if (unique == null) {
            unique = Boolean.FALSE;
        }
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
