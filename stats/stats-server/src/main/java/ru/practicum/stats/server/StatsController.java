package ru.practicum.stats.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.EndpointHitDtoRequest;
import ru.practicum.stats.dto.EndpointHitDtoResponse;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(code = HttpStatus.CREATED)
    public EndpointHitDtoResponse add(@RequestBody @Valid EndpointHitDtoRequest dtoRequest) {
        EndpointHit endpointHit = EndpointHitMapper.toEndpointHit(dtoRequest);
        return EndpointHitMapper.toEndpointHitDto(statsService.add(endpointHit));
    }

    @GetMapping("/stats")
    private List<ViewStats> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                     @RequestParam(required = false) String uris,
                                     @RequestParam(required = false) Boolean unique) {
        log.info("Получен запрос GET /stats с параметрами start = {}, end = {}, url = {}, unique = {}", start, end, uris, unique);
        List<String> uriList = uris == null ? Collections.emptyList() : Arrays.asList(uris.split(","));
        List<ViewStats> stats = statsService.getStats(start, end, uriList, unique);
        return stats;
    }
}
