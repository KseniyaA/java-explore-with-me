package ru.practicum.ewm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.stats.StatsClientProvider;
import ru.practicum.stats.dto.EndpointHitDtoRequest;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class TestStatsClientController {
    @Autowired
    private StatsClientProvider statsClientProvider;

    @GetMapping("/events")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Object> get(HttpServletRequest request) {
        EndpointHitDtoRequest ewm = EndpointHitDtoRequest.builder()
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .app("ewm").build();
        return statsClientProvider.getClient().sendStat(ewm);
    }

}
