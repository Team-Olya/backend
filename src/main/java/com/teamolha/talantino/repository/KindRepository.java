package com.teamolha.talantino.repository;

import com.teamolha.talantino.model.entity.Kind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KindRepository extends JpaRepository<Kind, Long> {
    boolean existsByKindIgnoreCase(String kind);
    Kind findByKindIgnoreCase(String kind);
}
