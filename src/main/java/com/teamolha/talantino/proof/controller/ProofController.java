package com.teamolha.talantino.proof.controller;

import com.teamolha.talantino.proof.model.response.TalentProofList;
import com.teamolha.talantino.proof.service.ProofService;
import com.teamolha.talantino.validation.ProofSort;
import com.teamolha.talantino.validation.ProofStatus;
import com.teamolha.talantino.validation.SortType;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@AllArgsConstructor
public class ProofController {
    ProofService proofService;

    @GetMapping("/talents/{talent-id}/proofs")
    TalentProofList getTalentProofs (
            Authentication auth,
            @PathVariable("talent-id") Long talentId,
            @RequestParam(required = false, defaultValue = "date") @Valid @ProofSort String sort,
            @RequestParam(required = false, defaultValue = "desc")  @Valid @SortType String type,
            @RequestParam(required = false, defaultValue = "PUBLISHED") @Valid @ProofStatus String status,
            @RequestParam(required = false, defaultValue = "9") Integer amount,
            @RequestParam(required = false, defaultValue = "0") Integer page
    ) {
        return proofService.talentProofs(auth.getName(), sort, type, status, amount, page, talentId);
    }
}
