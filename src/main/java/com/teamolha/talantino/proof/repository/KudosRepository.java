package com.teamolha.talantino.proof.repository;

import com.teamolha.talantino.proof.model.entity.Kudos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KudosRepository extends JpaRepository<Kudos, Long> {

    boolean existsBySponsorIdAndProofId(Long sponsorId, Long proofId);


}