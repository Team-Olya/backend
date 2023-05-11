package com.teamolha.talantino.sponsor.model.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SponsorUpdateRequest (
        @Pattern(regexp="^[a-zA-Z]*$")
        @Size(min = 2, max = 24)
        String name,
        @Pattern(regexp="^[a-zA-Z]*$")
        @Size(min = 2, max = 24)
        String surname,
        String avatar
) {
}
