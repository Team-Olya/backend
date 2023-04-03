package com.teamolha.talantino.talent.repository;

import com.teamolha.talantino.talent.model.entity.Talent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TalentRepository extends JpaRepository<Talent, Long> {
    Optional<Talent> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    List<Talent> findAllByOrderByIdDesc(Pageable pageable);

    @Query(value = "SELECT prev_value FROM (SELECT *, LAG(id) OVER " +
            "(ORDER BY id) AS prev_value FROM talent) subquery WHERE id=:id",
            nativeQuery = true)
    Long findPrevTalent(long id);

    @Query(value = "SELECT next_value FROM (SELECT *, LEAD(id) OVER " +
            "(ORDER BY id) AS next_value FROM talent) subquery WHERE id=:id",
            nativeQuery = true)
    Long findNextTalent(long id);

}
