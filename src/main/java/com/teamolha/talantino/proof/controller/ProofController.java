package com.teamolha.talantino.proof.controller;

import com.teamolha.talantino.proof.model.request.ProofRequest;
import com.teamolha.talantino.proof.model.response.KudosList;
import com.teamolha.talantino.proof.model.response.ProofDTO;
import com.teamolha.talantino.proof.model.response.ProofsPageDTO;
import com.teamolha.talantino.proof.model.response.TalentProofList;
import com.teamolha.talantino.proof.service.ProofService;
import com.teamolha.talantino.general.validation.ProofSort;
import com.teamolha.talantino.general.validation.ProofStatus;
import com.teamolha.talantino.general.validation.SortType;
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
public class ProofController {
    ProofService proofService;

    @GetMapping("/talents/{talent-id}/proofs")
    TalentProofList getTalentProofs(
            Authentication auth,
            @PathVariable("talent-id") Long talentId,
            @RequestParam(required = false, defaultValue = "date") @Valid @ProofSort String sort,
            @RequestParam(required = false, defaultValue = "desc") @Valid @SortType String type,
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
            @RequestBody @Valid ProofRequest proof) {
        proofService.createProof(auth.getName(), talentId, proof);
    }

    @GetMapping("/proofs")
    public ProofsPageDTO getAllProofs(
            Authentication auth,
            @RequestParam(required = false, defaultValue = "date") @Valid @ProofSort String sort,
            @RequestParam(required = false, defaultValue = "desc") @Valid @SortType String type,
            @RequestParam(required = false, defaultValue = "9") Integer amount,
            @RequestParam(required = false, defaultValue = "0") Integer page
    ) {
        return proofService.pageProofs(auth, sort, type, page, amount);
    }

    @PatchMapping("talents/{talent-id}/proofs/{proof-id}")
    public ProofDTO updateProof(
            @PathVariable("talent-id") Long talentId,
            @PathVariable("proof-id") Long proofId,
            @RequestBody @Valid ProofRequest proofUpdateRequest,
            Authentication auth
    ) {
        return proofService.updateProof(auth.getName(), talentId, proofId, proofUpdateRequest);
    }

    @DeleteMapping("/talents/{talent-id}/proofs/{proof-id}")
    public void deleteProof(
            @PathVariable("talent-id") Long talentId,
            @PathVariable("proof-id") Long proofId,
            Authentication auth
    ) {
        proofService.deleteProof(talentId, proofId, auth.getName());
    }

    @GetMapping("/proofs/{proof-id}")
    public ProofDTO getProof(@PathVariable("proof-id") Long proofId) {
        return proofService.getProof(proofId);
    }

    @GetMapping("/proofs/{proof-id}/kudos")
    public KudosList getKudos(Authentication auth,
                              @PathVariable("proof-id") Long proofId) {
        return proofService.getKudos(auth, proofId);
    }

    @PostMapping("/proofs/{proof-id}/kudos")
    public void setKudos(Authentication auth,
                         @PathVariable("proof-id") Long proofId) {
        proofService.setKudos(auth, proofId);
    }
}
