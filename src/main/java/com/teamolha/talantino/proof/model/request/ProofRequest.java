package com.teamolha.talantino.proof.model.request;

import com.teamolha.talantino.general.validation.ProofStatus;
import com.teamolha.talantino.general.validation.Skills;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProofRequest(
        @Size(min = 2, max = 80)
        String title,
        @Size(min = 2, max = 2000)
        String description,
        String status,
        @Skills
        List<String> skills
) {
}
