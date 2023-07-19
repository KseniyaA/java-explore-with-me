package ru.practicum.ewm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
@Validated
public class TestStatsClientController {
    private final StatsClient statsClient = new StatsClient();

    @GetMapping("/events")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Object> get(HttpServletRequest request) {
        EndpointHitDto ewm = EndpointHitDto.builder()
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .app("ewm").build();
        return statsClient.sendStat(ewm);
    }

}
