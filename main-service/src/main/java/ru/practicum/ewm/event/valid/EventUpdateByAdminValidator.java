package ru.practicum.ewm.event.valid;

import ru.practicum.ewm.event.dto.UpdateEventAdminRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EventUpdateByAdminValidator implements ConstraintValidator<ValidateUpdateByAdminEvent, UpdateEventAdminRequest> {
    @Override
    public void initialize(ValidateUpdateByAdminEvent constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UpdateEventAdminRequest updateEventAdminRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (updateEventAdminRequest.getAnnotation() != null) {
            if (updateEventAdminRequest.getAnnotation().trim().isEmpty()) {
                return false;
            }
        }
        if (updateEventAdminRequest.getTitle() != null) {
            if (updateEventAdminRequest.getTitle().trim().isEmpty()) {
                return false;
            }
        }
        if (updateEventAdminRequest.getDescription() != null) {
            if (updateEventAdminRequest.getDescription().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
