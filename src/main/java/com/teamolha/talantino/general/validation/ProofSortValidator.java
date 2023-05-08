package com.teamolha.talantino.general.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class ProofSortValidator implements ConstraintValidator<ProofSort, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean valid = value.equals("date") || value.equals("title");
        if (!valid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "It can be sorted only by date or title");
        }
        return true;
    }
}
