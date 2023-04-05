package com.teamolha.talantino.validation;

import com.teamolha.talantino.proof.model.Status;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProofStatusValidator implements ConstraintValidator<ProofStatus, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean valid = value.equals(Status.DRAFT.name())
                || value.equals(Status.HIDDEN.name())
                || value.equals(Status.PUBLISHED.name())
                || value.equals("ALL");
        if (!valid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Proof status must be DRAFT, HIDDEN or PUBLISHED");
        }
        return true;
    }
}
