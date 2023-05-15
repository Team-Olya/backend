package com.teamolha.talantino.proof.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReportedProofDTO(
        Long id,
        LocalDateTime date,
        String title,
        String description,
        @JsonIgnore
        Long proofAuthorId,
        String proofAuthor,
        @JsonIgnore
        String proofAuthorAvatar,
        String reportedBy
) {
}
