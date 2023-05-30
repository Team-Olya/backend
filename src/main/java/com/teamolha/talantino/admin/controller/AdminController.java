package com.teamolha.talantino.admin.controller;

import com.teamolha.talantino.admin.model.request.CreateAdmin;
import com.teamolha.talantino.admin.model.response.*;
import com.teamolha.talantino.admin.service.AdminService;
import com.teamolha.talantino.skill.model.request.SkillDTO;
import com.teamolha.talantino.talent.model.response.KindDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/admin/talents")
    public AdminTalentsDTO getTalents(@RequestParam(value = "email", defaultValue = "") String email,
                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                      @RequestParam(value = "size", defaultValue = "10") int size) {
        return adminService.getTalents(email, page, size);
    }


    @GetMapping("/admin/talents/{talent-id}")
    public AdminTalentDTO getTalent(@PathVariable("talent-id") Long talentId) {
        return adminService.getTalent(talentId);
    }

    @DeleteMapping("/admin/talents/{talent-id}")
    public void deleteTalent(@PathVariable("talent-id") Long talentId) {
        adminService.deleteTalent(talentId);
    }

    @GetMapping("/admin/proofs")
    public AdminProofsDTO getProofs(@RequestParam(value = "title", defaultValue = "") String title,
                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", defaultValue = "10") int size) {
        return adminService.getProofs(title, page, size);
    }

    @GetMapping("/admin/proofs/{proof-id}")
    public AdminProofDTO getProof(@PathVariable("proof-id") Long proofId) {
        return adminService.getProof(proofId);
    }

    @DeleteMapping("/admin/proofs/{proof-id}")
    public void deleteProof(@PathVariable("proof-id") Long proofId) {
        adminService.deleteProof(proofId);
    }

    @PatchMapping("/admin/talents/kinds/{kind-id}")
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

    @GetMapping("/admin/sponsors")
    public AdminSponsorsDTO getSponsors(@RequestParam(value = "email", defaultValue = "") String email,
                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        return adminService.getSponsors(email, page, size);
    }

    @GetMapping("/admin/sponsors/{sponsor-id}")
    public AdminSponsorDTO getSponsors(@PathVariable("sponsor-id") Long sponsorId) {
        return adminService.getSponsor(sponsorId);
    }

    @PatchMapping("/admin/accounts/{account-id}")
    public void unbannedAccount(@PathVariable("account-id") Long accountId) {
        adminService.unbannedAccount(accountId);
    }
}
