package com.teamolha.talantino.proof.controller;

import com.teamolha.talantino.proof.model.response.ProofDTO;
import com.teamolha.talantino.proof.model.response.ProofsPageDTO;
import com.teamolha.talantino.proof.repository.ProofRepository;
import com.teamolha.talantino.proof.service.ProofService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ProofController {
    ProofService proofService;

    @GetMapping("/proofs")
    public ProofsPageDTO listProofs(
            @RequestParam(required = false, defaultValue = "desc") String sort,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "9") Integer count
    ) {
        return proofService.pageProofs(sort, page, count);
    }
}
