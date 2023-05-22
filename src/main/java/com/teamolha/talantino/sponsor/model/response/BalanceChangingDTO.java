package com.teamolha.talantino.sponsor.model.response;

import com.teamolha.talantino.talent.model.entity.Talent;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BalanceChangingDTO(
        long amount,
        LocalDateTime date,
        long talentId
) {
}
