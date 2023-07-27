package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.dto.EventShortDtoResponse;
import ru.practicum.ewm.user.valid.ValidateUserName;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidateUserName
public class CompilationDtoResponse {
    private Long id;
    private List<EventShortDtoResponse> events;
    private Boolean pinned;
    private String title;
}
