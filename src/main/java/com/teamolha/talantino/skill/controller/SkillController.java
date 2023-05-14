package com.teamolha.talantino.skill.controller;

import com.teamolha.talantino.skill.model.response.SkillListDTO;
import com.teamolha.talantino.skill.service.SkillService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Validated
public class SkillController {

    SkillService skillService;

    @GetMapping("/skills")
    SkillListDTO getSkillList(@RequestParam(required = false) String search) {
        return skillService.getSkillList(search);
    }

    @GetMapping("/proofs/{proof-id}/skills")
    SkillListDTO getProofSkills(@PathVariable("proof-id") Long proofId) {
        return skillService.getProofSkills(proofId);
    }

}
