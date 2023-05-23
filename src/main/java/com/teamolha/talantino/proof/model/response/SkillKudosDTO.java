package com.teamolha.talantino.proof.model.response;

import com.teamolha.talantino.skill.model.request.SkillDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record SkillKudosDTO (
        SkillDTO skill,
        List<KudosDTO> kudos
){
}
