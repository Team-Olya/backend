package com.teamolha.talantino.talent.model.response;

public record LoginResponse(
        long id,
        String token,
        String name,
        String surname,
        String avatar
) {
}
