package com.teamolha.talantino.proof.repository;

import com.teamolha.talantino.proof.model.entity.Proof;
import com.teamolha.talantino.talent.model.entity.Talent;
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

    List<Proof> findByTitleStartsWithIgnoreCase(String title, Pageable pageable);

    long countByTitleStartsWithIgnoreCase(String title);

    List<Proof> findByStatus(String status, Pageable pageable);

    List<Proof> findByStatus(String status);

    List<Proof> findByStatusAndTalent_Id(String status, long talentId, Pageable pageable);

    List<Proof> findByStatusAndTalent_Id(String status, long talentId);

    List<Proof> findByTalent_Id(long talentId, Pageable pageable);

    List<Proof> findByTalent_Id(long talentId);

    long deleteByTalent(Talent talent);

    void deleteByTalentId(Long talentId);

    @Query(value = "SELECT sponsor_id, SUM(amount) FROM kudos WHERE proof_id=:proofId GROUP BY sponsor_id",
            countQuery = "SELECT COUNT(DISTINCT sponsor_id) FROM kudos WHERE proof_id=:proofId",
            nativeQuery = true)
    List<Object[]> findSponsorsAndKudosOnProof(Pageable pageable, long proofId);
}