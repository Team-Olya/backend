package com.teamolha.talantino.talent.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record TalentUpdateRequest(
        @NotNull
        String name,
        @NotNull
        String surname,
        @NotNull
        String kind,
        @NotNull
        String avatar,
        @NotNull
        String description,
        @NotNull
        int experience,
        @NotNull
        String location,
        @NotNull
        List<String> links
) {
}
