package com.teamolha.talantino.proof.model.response;

import com.teamolha.talantino.skill.model.request.ProofSkillDTO;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ShortProofDTO(
        Long id,
        LocalDateTime date,
        String title,
        @Size(min = 2, max = 200)
        String description,
        List<ProofSkillDTO> skills
) {
}
