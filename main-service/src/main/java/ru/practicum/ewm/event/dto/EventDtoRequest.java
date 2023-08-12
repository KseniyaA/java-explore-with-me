package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.valid.ValidateAnnotationEvent;
import ru.practicum.ewm.event.valid.ValidateDescriptionEvent;
import ru.practicum.ewm.event.valid.ValidateEventDateEvent;
import ru.practicum.ewm.valid.Marker;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDtoRequest {

    @NotBlank(groups = {Marker.OnCreate.class},
        message = "Поле annotation не должно быть пустым")
    @ValidateAnnotationEvent(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @Size(min = 20, max = 2000, groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    private String annotation;

    @NotNull(groups = {Marker.OnCreate.class})
    private Long category;

    @NotNull(groups = Marker.OnCreate.class)
    @ValidateDescriptionEvent(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @Size(min = 20, max = 7000, groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    private String description;

    @NotNull(groups = {Marker.OnCreate.class})
    @Future(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @ValidateEventDateEvent(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull(groups = {Marker.OnCreate.class})
    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit; // 0 - без ограничений

    /**
     * Нужна ли пре-модерация заявок на участие. Если true, то все заявки будут ожидать подтверждения инициатором события.
     * Если false - то будут подтверждаться автоматически.
     */
    private Boolean requestModeration;

    @NotNull(groups = {Marker.OnCreate.class})
    @Size(min = 3, max = 120, groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    private String title;

    private String stateAction;
}
