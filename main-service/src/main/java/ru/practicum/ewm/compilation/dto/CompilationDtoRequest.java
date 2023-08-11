package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.compilation.valid.ValidateTitle;
import ru.practicum.ewm.valid.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidateTitle
public class CompilationDtoRequest {
    private Set<Long> events;

    private Boolean pinned;

    @NotBlank(message = "Поле title не должно быть пустым", groups = {Marker.OnCreate.class})
    @Size(min = 2, max = 50, groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    private String title;
}
