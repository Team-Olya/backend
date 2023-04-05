package com.teamolha.talantino.proof.model.response;

import java.util.Date;

public record ProofDTO(
        Long id,
        Date date,
        String title,
        String description,
        Long authorId,
        String status
) {
}
