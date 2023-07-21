package ru.practicum.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EndpointHitDtoRequest {
    @NotBlank(message = "App is mandatory")
    private String app;

    @NotBlank(message = "Uri is mandatory")
    private String uri;

    @NotBlank(message = "Ip is mandatory")
    private String ip;
}
