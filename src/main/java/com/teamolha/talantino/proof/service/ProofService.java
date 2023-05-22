package com.teamolha.talantino.proof.service;

import com.teamolha.talantino.proof.model.request.ProofRequest;
import com.teamolha.talantino.proof.model.response.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface ProofService {

    ProofsPageDTO pageProofs(Authentication auth, String sort, String type, int page, int count);

    TalentProofList talentProofs(Authentication auth, String sort, String type, String status, Integer amount, Integer page, Long id);

    void createProof(String email, Long talentId, ProofRequest proof);

    ProofDTO updateProof(String email, Long talentId, Long proofId, ProofRequest newProof);

    void deleteProof(Long talentId, Long proofId, String email);

    ProofDTO getProof(Long proofId);

    KudosList getKudos(Authentication auth, Long proofId, int page, int size);

    void setKudos(Authentication auth, Long proofId, int amount);

    ReportedProofDTO reportProof(Authentication auth, Long proofId, HttpServletRequest request);

    void rejectReport(Long reportId);

    void approveReport(Long reportId);
}
