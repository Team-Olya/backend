package com.teamolha.talantino.admin.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record AdminTalentsDTO(
        long amount,
        List<AdminTalentDTO> talents
) {
}
