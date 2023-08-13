package ru.practicum.ewm.user.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserNameValidator.class)
public @interface ValidateUserName {
    String message() default "Наименование не должно состоять из пробелов";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
