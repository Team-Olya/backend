package com.teamolha.talantino.sponsor.model.response;

import lombok.Builder;

@Builder
public record SponsorProfileResponse(
        String role,
        Long id,
        String name,
        String surname,
        String avatar,
        Long balance,
        Long totalKudosed,
        Long totalSpent
) {
}
