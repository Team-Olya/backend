package com.teamolha.talantino.proof.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record KudosList(
    long totalAmount,
    List<KudosDTO> kudos
) {
}
