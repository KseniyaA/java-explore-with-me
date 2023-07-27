package ru.practicum.ewm.event.valid;

import ru.practicum.ewm.event.dto.EventDtoRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EventAnnotationValidator implements ConstraintValidator<ValidateAnnotationEvent, EventDtoRequest> {

    @Override
    public void initialize(ValidateAnnotationEvent constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(EventDtoRequest eventDtoRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (eventDtoRequest.getAnnotation() != null) {
            if (eventDtoRequest.getAnnotation().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
