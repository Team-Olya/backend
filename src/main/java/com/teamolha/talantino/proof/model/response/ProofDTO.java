package com.teamolha.talantino.proof.model.response;

import lombok.Builder;

import java.util.Date;

@Builder
public record ProofDTO(
        Long id,
        Date date,
        String title,
        String description,
        Long authorId,
        String status
) {
}
