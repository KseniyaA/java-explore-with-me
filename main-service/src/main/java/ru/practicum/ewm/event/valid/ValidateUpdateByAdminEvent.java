package ru.practicum.ewm.event.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EventUpdateByAdminValidator.class)
public @interface ValidateUpdateByAdminEvent {
    String message() default "Ошибки входных данных";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
