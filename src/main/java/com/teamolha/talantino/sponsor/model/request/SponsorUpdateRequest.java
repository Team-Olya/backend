package com.teamolha.talantino.sponsor.model.request;

import jakarta.validation.constraints.Size;

public record SponsorUpdateRequest (
        @Size(min = 2, max = 24)
        String name,
        @Size(min = 2, max = 24)
        String surname,
        String avatar
) {
}
