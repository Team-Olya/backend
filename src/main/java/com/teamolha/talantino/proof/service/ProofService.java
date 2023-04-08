package com.teamolha.talantino.proof.service;

import com.teamolha.talantino.proof.model.request.CreateProof;
import com.teamolha.talantino.proof.model.response.ProofsPageDTO;
import com.teamolha.talantino.proof.model.response.TalentProofList;

public interface ProofService {

    ProofsPageDTO pageProofs(String sort, String type, int page, int count);

    TalentProofList talentProofs(String name, String sort, String type, String status, Integer amount, Integer page, Long id);

    void createProof(String email, Long talentId, CreateProof proof);
}
