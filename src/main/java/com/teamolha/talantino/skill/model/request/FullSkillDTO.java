package com.teamolha.talantino.skill.model.request;

import lombok.Builder;

@Builder
public record FullSkillDTO(
        Long id,
        String label,
        String icon,
        Long totalKudos
) {
}
