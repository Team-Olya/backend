package com.teamolha.talantino.admin.controller;

import com.teamolha.talantino.admin.model.request.CreateAdmin;
import com.teamolha.talantino.admin.model.request.DeleteTalent;
import com.teamolha.talantino.admin.service.AdminService;
import com.teamolha.talantino.proof.model.request.ProofRequest;
import com.teamolha.talantino.proof.model.response.ProofDTO;
import com.teamolha.talantino.skill.model.request.SkillDTO;
import com.teamolha.talantino.talent.model.request.TalentUpdateRequest;
import com.teamolha.talantino.talent.model.response.KindDTO;
import com.teamolha.talantino.talent.model.response.UpdatedTalentResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Validated
@AllArgsConstructor
public class AdminController {

    AdminService adminService;

    @PostMapping("/admin/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAdmin (@RequestBody @Valid CreateAdmin createAdmin) {
        log.error(createAdmin.toString());
        adminService.createAdmin(createAdmin);
    }

    @PostMapping("/admin/login")
    public String login (Authentication authentication) {
        return adminService.login(authentication);
    }

    @DeleteMapping("/admin/talent")
    public void deleteTalent(@RequestBody DeleteTalent deleteTalent) {
        adminService.deleteTalent(deleteTalent.email());
    }

    @DeleteMapping("/admin/talents/{talent-id}")
    public void deleteTalent(@PathVariable("talent-id") Long talentId) {
        adminService.deleteTalent(talentId);
    }

    @DeleteMapping("/admin/proofs/{proof-id}")
    public void deleteProof(@PathVariable("proof-id") Long proofId) {
        adminService.deleteProof(proofId);
    }

    @PatchMapping("/admin/talents/{talent-id}")
    public UpdatedTalentResponse editTalent(@PathVariable("talent-id") Long talentId,
                                            @RequestBody TalentUpdateRequest newTalent) {
        return adminService.editTalent(newTalent, talentId);
    }

    @PatchMapping("/admin/proofs/{proof-id}")
    public ProofDTO editProof(@PathVariable("proof-id") Long proofId,
                                @RequestBody ProofRequest newProof) {
        return adminService.editProof(newProof, proofId);
    }

    @PatchMapping("/admin/kind-of-talent/{kind-id}")
    public KindDTO editKind(@PathVariable("kind-id") Long id,
                              @RequestBody String newKind) {
        return adminService.editKind(id, newKind);
    }

    @PostMapping("/admin/skills")
    public void addSkill(@RequestBody SkillDTO skillDTO) {
        adminService.addSkill(skillDTO);
    }

    @DeleteMapping("/admin/skills/{skill-id}")
    public void deleteSkill(@PathVariable("skill-id") Long skillId) {
        adminService.deleteSkill(skillId);
    }
}
