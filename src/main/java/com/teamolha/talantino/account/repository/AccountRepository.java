package com.teamolha.talantino.account.repository;

import com.teamolha.talantino.account.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmailIgnoreCase(String email);

    Optional<Account> findByVerificationToken(String token);
}