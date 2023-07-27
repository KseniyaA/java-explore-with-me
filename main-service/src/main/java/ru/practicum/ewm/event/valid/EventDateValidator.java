package ru.practicum.ewm.event.valid;

import ru.practicum.ewm.event.dto.EventDtoRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EventDateValidator implements ConstraintValidator<ValidateEventDateEvent, EventDtoRequest> {

    @Override
    public void initialize(ValidateEventDateEvent constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(EventDtoRequest eventDtoRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (eventDtoRequest.getEventDate() != null) {
            if (eventDtoRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                return false;
            }
            if (eventDtoRequest.getEventDate().isBefore(LocalDateTime.now())) {
                return false;
            }
        }
        return true;
    }
}
