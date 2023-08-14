package ru.practicum.ewm.location;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.location.dto.LocationParamDtoRequest;
import ru.practicum.ewm.location.dto.LocationParamDtoResponse;

@UtilityClass
public class LocationParamMapper {
    public LocationParam toLocation(LocationParamDtoRequest dto) {
        return LocationParam.builder()
                .lat(dto.getLat())
                .lon(dto.getLon())
                .radius(dto.getRadius())
                .name(dto.getName())
                .build();
    }

    public LocationParamDtoResponse toLocationDtoResponse(LocationParam location) {
        return LocationParamDtoResponse.builder()
                .id(location.getId())
                .lat(location.getLat())
                .lon(location.getLon())
                .radius(location.getRadius())
                .name(location.getName())
                .build();
    }
}
