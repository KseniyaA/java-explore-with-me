package ru.practicum.ewm.event.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EventAnnotationValidator implements ConstraintValidator<ValidateAnnotationEvent, String> {

    @Override
    public void initialize(ValidateAnnotationEvent constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String annotation, ConstraintValidatorContext constraintValidatorContext) {
        if (annotation != null) {
            if (annotation.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
