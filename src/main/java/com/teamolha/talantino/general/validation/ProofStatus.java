package com.teamolha.talantino.general.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({PARAMETER, FIELD, LOCAL_VARIABLE})
@Retention(RUNTIME)
@Constraint(validatedBy = ProofStatusValidator.class)
@Documented
public @interface ProofStatus {

    String message() default "{Password.invalid}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}