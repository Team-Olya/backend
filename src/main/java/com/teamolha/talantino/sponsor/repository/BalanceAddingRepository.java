package com.teamolha.talantino.sponsor.repository;

import com.teamolha.talantino.sponsor.model.entity.BalanceAdding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BalanceAddingRepository extends JpaRepository<BalanceAdding, Long> {
    List<BalanceAdding> findAllBySponsorId(Long sponsorId);
}