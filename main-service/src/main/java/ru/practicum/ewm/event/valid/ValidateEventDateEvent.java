package ru.practicum.ewm.event.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EventDateValidator.class)
public @interface ValidateEventDateEvent {
    String message() default "Дата и время события не может быть раньше, чем через два часа от текущего момента. " +
            "Дата и время события не может быть в прошлом.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
