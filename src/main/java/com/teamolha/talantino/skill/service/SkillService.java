package com.teamolha.talantino.skill.service;

import com.teamolha.talantino.skill.model.response.SkillListDTO;

public interface SkillService {
    SkillListDTO getSkillList(String search);

    SkillListDTO getProofSkills(Long proofId);
}
