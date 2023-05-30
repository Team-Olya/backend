package com.teamolha.talantino.proof.repository;

import com.teamolha.talantino.proof.model.entity.Proof;
import com.teamolha.talantino.talent.model.entity.Talent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT p FROM Proof as p JOIN Talent as t" +
            " ON p.talent.id = t.id WHERE p.status='PUBLISHED' AND t.accountStatus='ACTIVE'")
    List<Proof> findByStatus(Pageable pageable);

    @Query("SELECT p FROM Proof as p JOIN Talent as t" +
            " ON p.talent.id = t.id WHERE p.status='PUBLISHED' AND t.accountStatus='ACTIVE'")
    List<Proof> findByStatus();

    List<Proof> findByStatusAndTalent_Id(String status, long talentId, Pageable pageable);

    List<Proof> findByStatusAndTalent_Id(String status, long talentId);

    List<Proof> findByTalent_Id(long talentId, Pageable pageable);

    List<Proof> findByTalent_Id(long talentId);

    long deleteByTalent(Talent talent);

    void deleteByTalentId(Long talentId);

    @Query("SELECT k.sponsorId, SUM(k.amount), MAX(k.id) FROM Kudos k WHERE k.proofId = :proofId GROUP BY k.id ORDER BY SUM(k.amount) DESC")
    List<Object[]> findSponsorsAndKudosOnProof(@Param("proofId") Long proofId, Pageable pageable);


}