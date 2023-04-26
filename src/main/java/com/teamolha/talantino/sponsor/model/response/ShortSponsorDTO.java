package com.teamolha.talantino.sponsor.model.response;

import lombok.Builder;

@Builder
public record ShortSponsorDTO(
        String name,
        String surname,
        String avatar
) {
}
