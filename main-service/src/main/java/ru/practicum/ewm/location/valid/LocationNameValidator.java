package ru.practicum.ewm.location.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocationNameValidator implements ConstraintValidator<ValidateLocationName, String> {

    @Override
    public void initialize(ValidateLocationName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String locationName, ConstraintValidatorContext constraintValidatorContext) {
        if (locationName != null) {
            if (locationName.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
