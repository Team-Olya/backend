package com.teamolha.talantino.proof.model.response;

import com.teamolha.talantino.skill.model.request.ProofSkillDTO;
import com.teamolha.talantino.talent.model.response.ProofAuthorDTO;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ProofDTO(
        Long id,
        LocalDateTime date,
        String title,
        String description,
        ProofAuthorDTO author,
        String status,
        Integer totalKudos,
        Integer totalKudosFromSponsor,
        Boolean isKudosed,
        List<ProofSkillDTO> skills
) {
}
