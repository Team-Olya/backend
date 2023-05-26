package com.teamolha.talantino.talent.repository;

import com.teamolha.talantino.talent.model.entity.ReportTalent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportTalentRepository extends JpaRepository<ReportTalent, Long> {
}
