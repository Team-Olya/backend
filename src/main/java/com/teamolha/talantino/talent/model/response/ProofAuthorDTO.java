package com.teamolha.talantino.talent.model.response;

import lombok.Builder;

@Builder
public record ProofAuthorDTO(
        Long id,
        String name,
        String surname,
        String avatar
) {
}
