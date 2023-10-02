package ru.practicum.main.service.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = AfterDeltaHoursFromNowValidator.class)
@Documented
public @interface AfterDeltaHoursFromNow {
    String message() default "{eventDate must be later then now}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int deltaHours() default 0;
}
