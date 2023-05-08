package com.teamolha.talantino.skill.repository;

import com.teamolha.talantino.skill.model.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    boolean existsByLabelIgnoreCase(String label);
}
