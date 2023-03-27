package com.teamolha.talantino.model.response;

public record LoginResponse(
        long id,
        String token,
        String name,
        String surname,
        String avatar
) {
}
