package ru.practicum.ewm.compilation.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TitleValidator.class)
public @interface ValidateTitle {
    String message() default "Поле title не должно быть пустым или состоять из пробелов";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
