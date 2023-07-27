package ru.practicum.ewm.category.valid;

import ru.practicum.ewm.category.dto.CategoryDtoRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CategoryNameValidator implements ConstraintValidator<ValidateCategoryName, CategoryDtoRequest> {

    @Override
    public void initialize(ValidateCategoryName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CategoryDtoRequest categoryDtoRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (categoryDtoRequest.getName() == null || categoryDtoRequest.getName().trim().isEmpty()) {
            return false;
        }
        return true;
    }
}
