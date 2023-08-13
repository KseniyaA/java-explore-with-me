package ru.practicum.ewm.user.valid;

import ru.practicum.ewm.user.dto.UserDtoRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserNameValidator implements ConstraintValidator<ValidateUserName, UserDtoRequest> {

    @Override
    public void initialize(ValidateUserName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserDtoRequest userDtoRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (userDtoRequest.getName() == null || userDtoRequest.getName().trim().isEmpty()) {
            return false;
        }
        return true;
    }
}
