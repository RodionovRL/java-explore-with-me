package ru.practicum.main.service.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class AfterDeltaHoursFromNowValidator implements ConstraintValidator<AfterDeltaHoursFromNow, LocalDateTime> {
    private int deltaHours;

    @Override
    public void initialize(AfterDeltaHoursFromNow constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime localDateTime, ConstraintValidatorContext constraintValidatorContext) {
        if (localDateTime == null) {
            return true;
        }
        return !(localDateTime.isBefore(LocalDateTime.now().minusHours(deltaHours)));
    }
}
