package com.teamolha.talantino.account.repository;

import com.teamolha.talantino.account.model.entity.Account;
import com.teamolha.talantino.sponsor.model.entity.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmailIgnoreCase(String email);

    Optional<Account> findByVerificationToken(String token);

    Optional<Account> findByDeletionToken(String deletionToken);

    void deleteAllByVerificationExpireDate(Date currentDate);

    void deleteAllByVerificationExpireDateLessThanEqual(Date deletionDate);

    List<Account> findByVerificationExpireDateLessThanEqual(Date verificationExpireDate);

    List<Account> findByDeletionDateLessThanEqual(Date deletionDate);

    void deleteAllByDeletionDateLessThanEqual(Date deletionDate);
}