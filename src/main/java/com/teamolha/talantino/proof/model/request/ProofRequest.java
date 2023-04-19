package com.teamolha.talantino.proof.model.request;

import jakarta.validation.constraints.Size;

public record ProofRequest(
        @Size(min = 2, max = 80)
        String title,
        @Size(min = 2, max = 2000)
        String description,
//        @ProofStatus
        String status
) {
}
