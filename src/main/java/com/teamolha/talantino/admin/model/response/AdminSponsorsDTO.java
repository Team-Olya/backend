package com.teamolha.talantino.admin.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record AdminSponsorsDTO(
        long amount,
        List<AdminSponsorDTO> sponsors
) {
}
