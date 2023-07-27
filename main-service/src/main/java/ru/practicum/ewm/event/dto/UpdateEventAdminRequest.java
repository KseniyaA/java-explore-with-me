package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.valid.ValidateUpdateByAdminEvent;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ValidateUpdateByAdminEvent
public class UpdateEventAdminRequest {

    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit; // 0 - без ограничений

    /**
     * Нужна ли пре-модерация заявок на участие. Если true, то все заявки будут ожидать подтверждения инициатором события.
     * Если false - то будут подтверждаться автоматически.
     */
    private Boolean requestModeration;

    @Size(min = 3, max = 120)
    private String title;

    private String stateAction;
}
