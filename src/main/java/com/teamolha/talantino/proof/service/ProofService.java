package com.teamolha.talantino.proof.service;

import com.teamolha.talantino.proof.model.request.ProofRequest;
import com.teamolha.talantino.proof.model.response.ProofDTO;
import com.teamolha.talantino.proof.model.response.ProofsPageDTO;
import com.teamolha.talantino.proof.model.response.TalentProofList;
import org.springframework.security.core.Authentication;

public interface ProofService {

    ProofsPageDTO pageProofs(Authentication auth, String sort, String type, int page, int count);

    TalentProofList talentProofs(String name, String sort, String type, String status, Integer amount, Integer page, Long id);

    void createProof(String email, Long talentId, ProofRequest proof);

    ProofDTO updateProof(String email, Long talentId, Long proofId, ProofRequest newProof);

    void deleteProof(Long talentId, Long proofId, String email);

    ProofDTO getProof(Long proofId);

    int getNumberOfKudos(Long proofId);

    void setKudos(Authentication auth, Long proofId, int amount);
}
