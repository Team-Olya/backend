package com.teamolha.talantino.proof.service;

import com.teamolha.talantino.proof.model.response.ProofsPageDTO;
import com.teamolha.talantino.proof.model.response.TalentProofList;

public interface ProofService {

    ProofsPageDTO pageProofs(String sort, int page, int count);

    TalentProofList talentProofs(String name, String sort, String type, String status, Integer amount, Integer page, Long id);
}
