package com.teamolha.talantino.admin.service;

import com.teamolha.talantino.admin.model.request.CreateAdmin;
import com.teamolha.talantino.admin.model.response.*;
import com.teamolha.talantino.skill.model.request.SkillDTO;
import com.teamolha.talantino.talent.model.response.KindDTO;

public interface AdminService {
    void createAdmin(CreateAdmin createAdmin);

    void deleteTalent(Long talentId);

    void deleteProof(Long proofId);

    KindDTO editKind(Long id, String newKind);

    void addSkill(SkillDTO skillDTO);

    void deleteSkill(Long skillId);

    AdminTalentDTO getTalent(Long talentId);

    AdminTalentsDTO getTalents(String email, int page, int size);

    AdminProofsDTO getProofs(String title, int page, int size);

    AdminProofDTO getProof(Long proofId);

    AdminSponsorsDTO getSponsors(String email, int page, int size);

    AdminSponsorDTO getSponsor(Long sponsorId);

    void unbannedAccount(Long accountId);
}
