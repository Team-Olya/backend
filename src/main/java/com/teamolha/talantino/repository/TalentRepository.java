package com.teamolha.talantino.repository;

import com.teamolha.talantino.model.entity.Talent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TalentRepository extends JpaRepository<Talent, Long> {
    Optional<Talent> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    List<Talent> findAllByOrderByIdDesc(Pageable pageable);
}
