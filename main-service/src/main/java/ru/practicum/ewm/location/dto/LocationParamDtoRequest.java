package ru.practicum.ewm.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.location.valid.ValidateLocationName;
import ru.practicum.ewm.valid.Marker;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationParamDtoRequest {
    @NotNull(groups = {Marker.OnCreate.class}, message = "Поле lat не должно быть пустым")
    private Double lat;

    @NotNull(groups = {Marker.OnCreate.class}, message = "Поле lon не должно быть пустым")
    private Double lon;

    @NotNull(groups = {Marker.OnCreate.class}, message = "Поле radius не должно быть пустым")
    private Double radius;

    @NotNull(groups = {Marker.OnCreate.class}, message = "Поле name не должно быть пустым")
    @ValidateLocationName(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @Size(min = 3)
    private String name;
}
