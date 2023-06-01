package com.teamolha.talantino.proof.repository;

import com.teamolha.talantino.account.model.entity.Account;
import com.teamolha.talantino.proof.model.entity.Proof;
import com.teamolha.talantino.proof.model.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByProofAndAccount(Proof proof, Account account);
}
