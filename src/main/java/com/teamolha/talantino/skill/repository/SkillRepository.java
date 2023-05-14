package com.teamolha.talantino.skill.repository;

import com.teamolha.talantino.skill.model.entity.Skill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    boolean existsByLabelIgnoreCase(String label);

    @Query(value = "SELECT * FROM Skill WHERE label LIKE :search%", nativeQuery = true)
    List<Skill> findSkillsStartingWith(String search);

    List<Skill> findByProofs_Id(Long id);

    Optional<Skill> findByLabelIgnoreCase(String label);



}
