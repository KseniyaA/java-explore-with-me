package ru.practicum.ewm.location.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocationNameValidator.class)
public @interface ValidateLocationName {
    String message() default "Поле Наименование не должно быть пустым или состоять из пробелов";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
