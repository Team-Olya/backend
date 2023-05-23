package com.teamolha.talantino.talent.model.response;

import com.teamolha.talantino.proof.model.response.ProofDTO;
import com.teamolha.talantino.skill.model.request.SkillDTO;
import lombok.Builder;

@Builder
public record TalentStatistic(
        long totalAmount,
        SkillDTO skill,
        ProofDTO proof
) {
}
