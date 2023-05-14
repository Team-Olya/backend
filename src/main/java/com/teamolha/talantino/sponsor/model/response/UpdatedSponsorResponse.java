package com.teamolha.talantino.sponsor.model.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdatedSponsorResponse (
        long id,
        @NotNull
        String name,
        @NotNull
        String surname,
        @NotNull
        Long balance,
        @NotNull
        String avatar
) {
}
