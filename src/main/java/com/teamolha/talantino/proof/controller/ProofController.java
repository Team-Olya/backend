package com.teamolha.talantino.proof.controller;

import com.teamolha.talantino.proof.model.request.ProofRequest;
import com.teamolha.talantino.proof.model.response.ProofDTO;
import com.teamolha.talantino.proof.model.response.ProofsPageDTO;
import com.teamolha.talantino.proof.model.response.TalentProofList;
import com.teamolha.talantino.proof.service.ProofService;
import com.teamolha.talantino.validation.ProofSort;
import com.teamolha.talantino.validation.ProofStatus;
import com.teamolha.talantino.validation.SortType;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/talents/{talent-id}/proofs")
    @ResponseStatus(HttpStatus.CREATED)
    void createProof(
            Authentication auth,
            @PathVariable("talent-id") Long talentId,
            @RequestBody @Valid ProofRequest proof){
        proofService.createProof(auth.getName(), talentId, proof);
    }

    @GetMapping("/proofs")
    public ProofsPageDTO getAllProofs(
            @RequestParam(required = false, defaultValue = "date") @Valid @ProofSort String sort,
            @RequestParam(required = false, defaultValue = "desc") @Valid @SortType String type,
            @RequestParam(required = false, defaultValue = "9") Integer amount,
            @RequestParam(required = false, defaultValue = "0") Integer page
    ) {
        return proofService.pageProofs(sort, type, page, amount);
    }

    @PatchMapping("talents/{talent-id}/proofs/{proof-id}")
    public ProofDTO updateProof(
            @PathVariable("talent-id") Long talentId,
            @PathVariable("proof-id") Long proofId,
            @RequestBody @Valid ProofRequest proofUpdateRequest,
            Authentication auth
    ){
        return proofService.updateProof(auth.getName(), talentId, proofId, proofUpdateRequest);
    }
}
