package com.teamolha.talantino.sponsor.repository;

import com.teamolha.talantino.sponsor.model.entity.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SponsorRepository extends JpaRepository<Sponsor, Long> {
    boolean existsByEmailIgnoreCase(String email);

    Optional<Sponsor> findByEmailIgnoreCase(String email);

    Optional<Sponsor> findByDeletionToken(String deletionToken);

    void deleteAllByDeletionDateLessThanEqual(Date deletionDate);
}