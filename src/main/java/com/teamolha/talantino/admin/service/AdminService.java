package com.teamolha.talantino.admin.service;

import com.teamolha.talantino.admin.model.request.CreateAdmin;
import com.teamolha.talantino.proof.model.request.ProofRequest;
import com.teamolha.talantino.proof.model.response.ProofDTO;
import com.teamolha.talantino.skill.model.request.ProofSkillDTO;
import com.teamolha.talantino.talent.model.request.TalentUpdateRequest;
import com.teamolha.talantino.talent.model.response.KindDTO;
import com.teamolha.talantino.talent.model.response.UpdatedTalentResponse;
import org.springframework.security.core.Authentication;

public interface AdminService {
    void createAdmin(CreateAdmin createAdmin);

    String login(Authentication authentication);

    void deleteTalent(String email);

    void deleteTalent(Long talentId);

    void deleteProof(Long proofId);

    UpdatedTalentResponse editTalent(TalentUpdateRequest newTalent, Long talentId);

    ProofDTO editProof(ProofRequest newProof, Long proofId);

    KindDTO editKind(Long id, String newKind);

    void addSkill(ProofSkillDTO proofSkillDTO);

    void deleteSkill(Long skillId);
}
