package ru.practicum.ewm.event.valid;

import ru.practicum.ewm.event.dto.EventDtoRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EventDescriptionValidator implements ConstraintValidator<ValidateDescriptionEvent, EventDtoRequest> {

    @Override
    public void initialize(ValidateDescriptionEvent constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(EventDtoRequest eventDtoRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (eventDtoRequest.getDescription() != null) {
            if (eventDtoRequest.getDescription().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
