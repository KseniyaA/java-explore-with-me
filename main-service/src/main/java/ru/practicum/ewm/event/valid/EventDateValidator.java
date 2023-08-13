package ru.practicum.ewm.event.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EventDateValidator implements ConstraintValidator<ValidateEventDateEvent, LocalDateTime> {

    @Override
    public void initialize(ValidateEventDateEvent constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime eventDate, ConstraintValidatorContext constraintValidatorContext) {
        if (eventDate != null) {
            if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                return false;
            }
            if (eventDate.isBefore(LocalDateTime.now())) {
                return false;
            }
        }
        return true;
    }
}
