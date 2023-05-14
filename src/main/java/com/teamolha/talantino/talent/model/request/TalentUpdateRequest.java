package com.teamolha.talantino.talent.model.request;

import com.teamolha.talantino.general.validation.Skills;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record TalentUpdateRequest(
        String name,
        String surname,
        String kind,
        String avatar,
        String description,
        Integer experience,
        String location,
        List<String> links,
        @Skills
        List<String> skills
) {
}
