package com.teamolha.talantino.general.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = SkillsValidator.class)
@Documented
public @interface Skills {

    String message() default "{Skill.invalid}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
