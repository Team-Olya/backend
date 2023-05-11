package com.teamolha.talantino.proof.model.response;

import com.teamolha.talantino.skill.model.request.SkillDTO;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ProofDTO(
        Long id,
        LocalDateTime date,
        String title,
        String description,
        Long authorId,
        String status,
        List<SkillDTO> skills
        Integer totalKudos,
        Integer totalKudosFromSponsor,
        Boolean isKudosed
) {
}
