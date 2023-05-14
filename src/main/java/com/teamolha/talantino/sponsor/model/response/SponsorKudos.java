package com.teamolha.talantino.sponsor.model.response;

import com.teamolha.talantino.proof.model.response.ProofDTO;
import lombok.Builder;

@Builder
public record SponsorKudos (
        ProofDTO proofDTO,
        int amount
) {
}
