package com.teamolha.talantino.sponsor.model.request;

public record SponsorUpdateRequest (
        String name,
        String surname,
        String avatar
) {
}
