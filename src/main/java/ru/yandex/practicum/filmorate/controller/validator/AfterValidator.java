package ru.yandex.practicum.filmorate.controller.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class AfterValidator implements ConstraintValidator<After, LocalDate> {

    private int day;
    private int month;
    private int year;
    private boolean include;
    private LocalDate specifiedLocalDate;

    @Override
    public void initialize(After parameters) {
        day = parameters.day();
        month = parameters.month();
        year = parameters.year();
        include = parameters.include();
        validateParameters();
        specifiedLocalDate = LocalDate.of(year, month, day);
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (localDate == null) return true;
        if (include) {
            return localDate.isEqual(specifiedLocalDate) || localDate.isAfter(specifiedLocalDate);
        } else {
            return localDate.isAfter(specifiedLocalDate);
        }
    }

    private void validateParameters() {
        if (this.day <= 0) {
            throw new IllegalArgumentException("day value must be > 0");
        } else if (this.month <= 0) {
            throw new IllegalArgumentException("month value must be > 0");
        } else if (this.year <= 0) {
            throw new IllegalArgumentException("year value must be > 0");
        }
    }
}
