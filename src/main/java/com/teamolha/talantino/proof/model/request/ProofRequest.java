package com.teamolha.talantino.proof.model.request;

import com.teamolha.talantino.general.validation.ProofStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProofRequest(
        @NotNull
        @Size(min = 2, max = 80)
        String title,
        @NotNull
        @Size(min = 2, max = 2000)
        String description,
        @NotNull
        @ProofStatus
        String status
) {
}
