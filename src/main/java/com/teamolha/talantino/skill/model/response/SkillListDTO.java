package com.teamolha.talantino.skill.model.response;

import com.teamolha.talantino.skill.model.request.SkillDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record SkillListDTO(
        List<SkillDTO> skills
) {
}
