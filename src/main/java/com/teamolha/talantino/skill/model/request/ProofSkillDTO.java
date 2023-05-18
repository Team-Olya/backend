package com.teamolha.talantino.skill.model.request;

import lombok.Builder;

@Builder
public record ProofSkillDTO(
        Long id,
        String label,
        String icon,
        Integer totalKudos,
        Integer totalKudosFromSponsor,
        Boolean isKudosed
) {

}
