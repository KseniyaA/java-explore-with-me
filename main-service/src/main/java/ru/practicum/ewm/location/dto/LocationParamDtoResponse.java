package ru.practicum.ewm.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationParamDtoResponse {
    private Long id;
    private Double lat;
    private Double lon;
    private Double radius;
    private String name;
}
