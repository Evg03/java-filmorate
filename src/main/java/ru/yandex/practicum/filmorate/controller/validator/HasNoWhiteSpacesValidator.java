package ru.yandex.practicum.filmorate.controller.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HasNoWhiteSpacesValidator implements ConstraintValidator<HasNoWhiteSpaces,String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) return true;
        return !s.contains(" ");
    }
}
