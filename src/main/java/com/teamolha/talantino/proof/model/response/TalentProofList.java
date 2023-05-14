package com.teamolha.talantino.proof.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record TalentProofList(
        long totalAmount,
        List<ProofDTO> proofs
) {
}
