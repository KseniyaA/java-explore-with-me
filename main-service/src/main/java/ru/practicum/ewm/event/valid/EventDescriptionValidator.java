package ru.practicum.ewm.event.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EventDescriptionValidator implements ConstraintValidator<ValidateDescriptionEvent, String> {

    @Override
    public void initialize(ValidateDescriptionEvent constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String description, ConstraintValidatorContext constraintValidatorContext) {
        if (description != null) {
            if (description.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
