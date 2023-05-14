package com.teamolha.talantino.proof.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ProofsPageDTO(
        int totalAmount,
        List<ShortProofDTO> proofs
) {
}
