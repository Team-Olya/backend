package com.teamolha.talantino.skill.controller;

import com.teamolha.talantino.skill.model.response.SkillListDTO;
import com.teamolha.talantino.skill.service.SkillService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Validated
public class SkillController {

    private SkillService skillService;

    @GetMapping("/skills")
    public SkillListDTO getSkillList(@RequestParam(required = false) String search) {
        return skillService.getSkillList(search);
    }

    @GetMapping("/proofs/{proof-id}/skills")
    public SkillListDTO getProofSkills(@PathVariable("proof-id") Long proofId) {
        return skillService.getProofSkills(proofId);
    }

    @PostMapping("/proofs/{proof-id}/skills/{skill-id}/kudos")
    public void setKudosToSkill(Authentication auth,
                                @PathVariable("proof-id") Long proofId,
                                @PathVariable("skill-id") Long skillId,
                                @RequestParam(value = "amount", required = false, defaultValue = "1") int amount) {
        skillService.setKudosToSkill(auth, proofId, skillId, amount);
    }
}
