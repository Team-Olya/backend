package com.teamolha.talantino.general.validation;

import com.teamolha.talantino.skill.repository.SkillRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

public class SkillsValidator implements ConstraintValidator<Skills, List<String>> {

    @Autowired
    private SkillRepository skillRepository;

    @Override
    public boolean isValid(List<String> skills, ConstraintValidatorContext constraintValidatorContext) {
        List<String> invalidSkills = new ArrayList<>();

        for (String skill : skills) {
            if (!skillRepository.existsByLabelIgnoreCase(skill.toLowerCase())) {
                invalidSkills.add(skill);
            }
        }

        if (!invalidSkills.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Skills: "
                    + String.join(", ", invalidSkills)
                    + " don't exist!");
        }
        return true;
    }
}
