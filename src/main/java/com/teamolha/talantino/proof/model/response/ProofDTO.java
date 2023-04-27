package com.teamolha.talantino.proof.model.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProofDTO(
        Long id,
        LocalDateTime date,
        String title,
        String description,
        Long authorId,
        String status,
        Integer totalKudos,
        Integer totalKudosFromSponsor,
        Boolean isKudosed
) {
}
