package com.teamolha.talantino.proof.service;

import com.teamolha.talantino.proof.model.response.ProofsPageDTO;

public interface ProofService {

    ProofsPageDTO pageProofs(String sort, int page, int count);
}
