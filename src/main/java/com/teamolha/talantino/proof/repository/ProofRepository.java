package com.teamolha.talantino.proof.repository;

import com.teamolha.talantino.proof.model.entity.Proof;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProofRepository extends JpaRepository<Proof, Long> {
    @Query(value = "SELECT prev_value FROM (SELECT *, LAG(id) OVER " +
            "(ORDER BY id) AS prev_value FROM proof) subquery WHERE id=:id",
            nativeQuery = true)
    Long findPrevProofId(long id);

    @Query(value = "SELECT next_value FROM (SELECT *, LEAD(id) OVER " +
            "(ORDER BY id) AS next_value FROM proof) subquery WHERE id=:id",
            nativeQuery = true)
    Long findNextProofId(long id);

    List<Proof> findByStatus(String status, Pageable pageable);

    List<Proof> findByStatus(String status);
}