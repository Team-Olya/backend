package com.teamolha.talantino.sponsor.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record BalanceHistoryDTO(
        long totalAmount,
        List<BalanceChangingDTO> balanceChangings
) {
}
