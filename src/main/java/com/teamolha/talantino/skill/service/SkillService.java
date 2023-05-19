package com.teamolha.talantino.skill.service;

import com.teamolha.talantino.skill.model.response.SkillListDTO;
import org.springframework.security.core.Authentication;

public interface SkillService {
    SkillListDTO getSkillList(String search);

    SkillListDTO getProofSkills(Long proofId);

    void setKudosToSkill(Authentication auth, Long proofId, Long skillId, int amount);
}
