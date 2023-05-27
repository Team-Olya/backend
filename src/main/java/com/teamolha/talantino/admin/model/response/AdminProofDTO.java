package com.teamolha.talantino.admin.model.response;

import com.teamolha.talantino.proof.model.Status;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AdminProofDTO(
        Long proofId,
        Long authorId,
        LocalDateTime date,
        String title,
        String description,
        String status
) {
}
