package com.teamolha.talantino.sponsor.model.response;

import lombok.Builder;

@Builder
public record SponsorProfileResponse(
        Long id,
        String name,
        String surname,
        String avatar,
        Long balance
) {
}
