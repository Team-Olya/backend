package com.teamolha.talantino.talent.model.response;

import lombok.Builder;

@Builder
public record LoginResponse(
        Long id,
        String token,
        String name,
        String surname,
        String avatar
) {
}
