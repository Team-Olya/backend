package com.teamolha.talantino.skill.model.request;

import com.teamolha.talantino.skill.model.entity.Skill;

public record SkillDTO(
        Long id,
        String label,
        String icon
) {
    public SkillDTO(Skill skill) {
        this(skill.getId(), skill.getLabel(), skill.getIcon());
    }
}
