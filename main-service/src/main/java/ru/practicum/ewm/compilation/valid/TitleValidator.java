package ru.practicum.ewm.compilation.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TitleValidator implements ConstraintValidator<ValidateTitle, String> {

    @Override
    public void initialize(ValidateTitle constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String title, ConstraintValidatorContext constraintValidatorContext) {
        if (title != null) {
            if (title.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
