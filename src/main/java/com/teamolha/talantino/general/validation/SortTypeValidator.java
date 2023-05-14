package com.teamolha.talantino.general.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class SortTypeValidator implements ConstraintValidator<SortType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean valid = value.equals("desc") || value.equals("asc");
        if (!valid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Sort type must be asc or desc");
        }
        return true;
    }
}
