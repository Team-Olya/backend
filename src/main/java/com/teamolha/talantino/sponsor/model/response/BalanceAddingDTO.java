package com.teamolha.talantino.sponsor.model.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BalanceAddingDTO(
        int amount,
        LocalDateTime date
) {
}
