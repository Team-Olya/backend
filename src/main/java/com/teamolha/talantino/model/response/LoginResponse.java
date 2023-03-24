package com.teamolha.talantino.model.response;

public record LoginResponse(
        String token,
        String name,
        String surname,
        String avatar
) {
}
