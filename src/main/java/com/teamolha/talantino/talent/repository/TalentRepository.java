package com.teamolha.talantino.talent.repository;

import com.teamolha.talantino.account.model.entity.AccountStatus;
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

    Optional<Talent> findByVerificationToken(String verificationToken);

    boolean existsByEmailIgnoreCase(String email);

    void deleteTalentByEmailIgnoreCase(String email);

    List<Talent> findByAccountStatusOrAccountStatusIsNullOrderByIdDesc(Pageable pageable, AccountStatus accountStatus);

    List<Talent> findByAccountStatusOrAccountStatusIsNull(AccountStatus accountStatus);

    @Query(value = "SELECT prev_value FROM (SELECT *, LAG(id) OVER (ORDER BY id) AS prev_value FROM account " +
            "INNER JOIN ACCOUNT_AUTHORITIES auth ON account.id = auth.account_id WHERE auth.AUTHORITIES = 'TALENT')" +
            " subquery WHERE id=:id",
            nativeQuery = true)
    Long findPrevTalent(long id);

    @Query(value = "SELECT next_value FROM (SELECT *, LEAD(id) OVER (ORDER BY id) AS next_value FROM account " +
            "INNER JOIN ACCOUNT_AUTHORITIES auth ON account.id = auth.account_id WHERE auth.AUTHORITIES = 'TALENT')" +
            " subquery WHERE id=:id",
            nativeQuery = true)
    Long findNextTalent(long id);

    @Query("SELECT DISTINCT t FROM Talent t JOIN t.skills s WHERE s IN :skillList GROUP BY t.id HAVING COUNT(DISTINCT s) = :skillCount")
    List<Talent> findAllBySkills(@Param("skillList") List<Skill> skillList, @Param("skillCount") Long skillCount, Pageable pageable);

    @Query("SELECT t FROM Talent t JOIN t.skills s WHERE s IN :skillList GROUP BY t.id HAVING COUNT(DISTINCT s) = :skillCount")
    List<Talent> findAllBySkills(@Param("skillList") List<Skill> skillList, @Param("skillCount") Long skillCount);
}
