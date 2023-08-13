package ru.practicum.ewm.event.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EventDescriptionValidator.class)
public @interface ValidateDescriptionEvent {
    String message() default "Описание не должно быть пустым или состоять из пробелов";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
