package ru.practicum.stats.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.EndpointHitDtoRequest;
import ru.practicum.stats.dto.EndpointHitDtoResponse;

import javax.validation.Valid;
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
    private List<ViewStats> getStats(@RequestParam String start,
                                     @RequestParam String end,
                                     @RequestParam(required = false) List<String> uris,
                                     @RequestParam(required = false) Boolean unique) {
        return statsService.getStats(start, end, uris, unique);
    }
}
