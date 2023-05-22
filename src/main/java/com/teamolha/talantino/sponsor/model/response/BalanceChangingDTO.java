package com.teamolha.talantino.sponsor.model.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BalanceChangingDTO(
        long amount,
        LocalDateTime date,
        Long proofId
) {
}
