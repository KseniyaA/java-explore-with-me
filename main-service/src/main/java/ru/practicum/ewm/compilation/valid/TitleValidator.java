package ru.practicum.ewm.compilation.valid;

import ru.practicum.ewm.compilation.dto.CompilationDtoRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TitleValidator implements ConstraintValidator<ValidateTitle, CompilationDtoRequest> {

    @Override
    public void initialize(ValidateTitle constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CompilationDtoRequest dtoRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (dtoRequest.getTitle() != null) {
            if (dtoRequest.getTitle().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
