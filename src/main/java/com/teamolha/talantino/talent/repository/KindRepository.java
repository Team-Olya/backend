package com.teamolha.talantino.talent.repository;

import com.teamolha.talantino.talent.model.entity.Kind;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface KindRepository extends JpaRepository<Kind, Long> {
    boolean existsByKindIgnoreCase(String kind);
    Kind findByKindIgnoreCase(String kind);

}
