package com.teamolha.talantino.sponsor.repository;

import com.teamolha.talantino.sponsor.model.entity.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SponsorRepository extends JpaRepository<Sponsor, Long> {
    boolean existsByEmailIgnoreCase(String email);

    Optional<Sponsor> findByEmailIgnoreCase(String email);
}