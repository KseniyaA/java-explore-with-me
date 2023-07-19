package ru.practicum.stats.dto;


import lombok.experimental.UtilityClass;
import ru.practicum.stats.server.EndpointHit;

@UtilityClass
public final class EndpointHitMapper {
    public EndpointHit toEndpointHit(EndpointHitDto dto) {
        return EndpointHit.builder()
                .app(dto.getApp())
                .ip(dto.getIp())
                .uri(dto.getUri())
                .build();
    }
}
