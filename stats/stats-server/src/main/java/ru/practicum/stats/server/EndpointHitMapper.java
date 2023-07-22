package ru.practicum.stats.server;

import lombok.experimental.UtilityClass;
import ru.practicum.stats.dto.EndpointHitDtoRequest;
import ru.practicum.stats.dto.EndpointHitDtoResponse;

@UtilityClass
public final class EndpointHitMapper {
    public EndpointHit toEndpointHit(EndpointHitDtoRequest dto) {
        return EndpointHit.builder()
                .app(dto.getApp())
                .ip(dto.getIp())
                .uri(dto.getUri())
                .build();
    }

    public EndpointHitDtoResponse toEndpointHitDto(EndpointHit endpointHit) {
        return EndpointHitDtoResponse.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .ip(endpointHit.getIp())
                .uri(endpointHit.getUri())
                .build();
    }
}
