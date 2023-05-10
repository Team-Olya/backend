package com.teamolha.talantino.talent.model.response;

import com.teamolha.talantino.skill.model.request.SkillDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record TalentProfileResponse(
        long id,

        String name,

        String surname,

        String email,

        String kind,

        String description,

        String avatar,

        int experience,

        String location,

        List<String> links,

        Long prevId,

        Long nextId,

        List<SkillDTO> skills
) {
}

