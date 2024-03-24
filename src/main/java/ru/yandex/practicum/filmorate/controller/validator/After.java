package ru.yandex.practicum.filmorate.controller.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {AfterValidator.class})
public @interface After {

    String message() default "{Date is incorrect}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int day();

    int month();

    int year();

    boolean include();
}
