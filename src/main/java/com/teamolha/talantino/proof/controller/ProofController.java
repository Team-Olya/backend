package com.teamolha.talantino.proof.controller;

import com.teamolha.talantino.proof.model.response.ProofDTO;
import com.teamolha.talantino.proof.repository.ProofRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ProofController {
//    ProofRepository proofRepository;
//
//    @GetMapping("/proofs/test")
//    ProofDTO test() {
//        var proof = proofRepository.findById(1L).get();
//        return new ProofDTO(
//                proof.getId(),
//                proof.getDate(),
//                proof.getTitle(),
//                proof.getDescription(),
//                proof.getTalent().getId(),
//                proof.getStatus()
//        );
//    }
}
