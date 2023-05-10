package com.teamolha.talantino.talent.model.response;

import com.teamolha.talantino.skill.model.request.SkillDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdatedTalentResponse (
        long id,
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
        List<String> links,
        List<SkillDTO> skills
) {
}
