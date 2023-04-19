package com.teamolha.talantino.proof.model.response;

import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ShortProofDTO(
        Long id,
        LocalDateTime date,
        String title,
        @Size(min = 2, max = 200)
        String description,
        Integer totalKudos,
        Boolean isKudosed,
        Boolean isAuthor
) {
}
