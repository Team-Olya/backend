package com.teamolha.talantino.talent.repository;

import com.teamolha.talantino.account.model.AccountStatus;
import com.teamolha.talantino.skill.model.entity.Skill;
import com.teamolha.talantino.talent.model.entity.Talent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TalentRepository extends JpaRepository<Talent, Long> {

    void deleteAllByVerificationExpireDate(Date currentDate);

    Optional<Talent> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    List<Talent> findByAccountStatusOrAccountStatusIsNullOrderByIdDesc(Pageable pageable, AccountStatus accountStatus);

    List<Talent> findByAccountStatusOrAccountStatusIsNull(AccountStatus accountStatus);

    @Query(value = "SELECT prev_value FROM (SELECT *, LAG(id) OVER (ORDER BY id) AS prev_value FROM account " +
            "INNER JOIN ACCOUNT_AUTHORITIES auth ON account.id = auth.account_id WHERE (account_status = 'ACTIVE' " +
            "OR account_status IS NULL) AND auth.AUTHORITIES = 'TALENT') subquery WHERE id = :id",
            nativeQuery = true)
    Long findPrevTalent(long id);

    @Query(value = "SELECT prev_value FROM (SELECT *, LEAD(id) OVER (ORDER BY id) AS prev_value FROM account " +
            "INNER JOIN ACCOUNT_AUTHORITIES auth ON account.id = auth.account_id WHERE (account_status = 'ACTIVE' " +
            "OR account_status IS NULL) AND auth.AUTHORITIES = 'TALENT') subquery WHERE id = :id",
            nativeQuery = true)
    Long findNextTalent(long id);

    @Query("SELECT DISTINCT t FROM Talent t JOIN t.skills s WHERE s IN :skillList GROUP BY " +
            "t.id, t.accountStatus, t.email, t.password, t.name, t.surname, t.verificationExpireDate, t.verificationToken," +
            "t.deletionDate, t.deletionToken" +
            " HAVING COUNT(DISTINCT s) = :skillCount")
    List<Talent> findAllBySkills(@Param("skillList") List<Skill> skillList, @Param("skillCount") Long skillCount, Pageable pageable);

    @Query("SELECT t FROM Talent t JOIN t.skills s WHERE s IN :skillList GROUP BY " +
            "t.id, t.accountStatus, t.email, t.password, t.name, t.surname, t.verificationExpireDate, t.verificationToken, " +
            "t.deletionDate, t.deletionToken" +
            " HAVING COUNT(DISTINCT s) = :skillCount")
    List<Talent> findAllBySkills(@Param("skillList") List<Skill> skillList, @Param("skillCount") Long skillCount);

    @Query("SELECT kudos.skillId FROM Talent talent JOIN talent.proofs proofs " +
            "JOIN proofs.kudos kudos WHERE talent.id = :talentId  GROUP BY kudos.skillId " +
            "ORDER BY COUNT(kudos.skillId) DESC LIMIT 1")
    Long findMostKudosedSkill(@Param("talentId") Long talentId);

    @Query("SELECT kudos.proofId FROM Talent talent JOIN talent.proofs proofs " +
            "JOIN proofs.kudos kudos WHERE talent.id = :talentId GROUP BY kudos.proofId " +
            "ORDER BY COUNT(kudos.proofId) DESC LIMIT 1")
    Long findMostKudosedProof(@Param("talentId") Long talentId);
}
