package com.teamolha.talantino.proof.model.request;

import com.teamolha.talantino.validation.ProofStatus;
import jakarta.validation.constraints.NotNull;

public record CreateProof(
    @NotNull
    String title,
    @NotNull
    String description,
    @NotNull
    @ProofStatus
    String status
){}
