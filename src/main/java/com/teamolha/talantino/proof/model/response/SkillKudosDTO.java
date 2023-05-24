package com.teamolha.talantino.proof.model.response;

import com.teamolha.talantino.skill.model.request.SkillDTO;
import lombok.Builder;

@Builder
public record SkillKudosDTO(
        SkillDTO skill,
        long amountOfKudos
) {
}
