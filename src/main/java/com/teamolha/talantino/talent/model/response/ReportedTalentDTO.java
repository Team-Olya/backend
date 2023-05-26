package com.teamolha.talantino.talent.model.response;

import lombok.Builder;

@Builder
public record ReportedTalentDTO(
        Long id,
        Long reportedTalentId,
        String fullName,
        String kind,
        String email,
        String description,
        String avatar,
        String location,
        String reportedBy
) {
}
