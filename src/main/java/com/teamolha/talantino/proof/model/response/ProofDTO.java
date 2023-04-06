package com.teamolha.talantino.proof.model.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
public record ProofDTO(
        Long id,
        LocalDateTime date,
        String title,
        String description,
        Long authorId,
        String status
) {
}
