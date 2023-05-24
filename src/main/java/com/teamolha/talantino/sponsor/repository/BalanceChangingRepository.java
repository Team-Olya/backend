package com.teamolha.talantino.sponsor.repository;

import com.teamolha.talantino.sponsor.model.entity.BalanceChanging;
import com.teamolha.talantino.talent.model.entity.Talent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BalanceChangingRepository extends JpaRepository<BalanceChanging, Long> {
    List<BalanceChanging> findBySponsorId(Long sponsorId, Pageable pageable);

    long countBySponsorId(Long sponsorId);

    List<BalanceChanging> findAllByTalent(Talent talent);


}