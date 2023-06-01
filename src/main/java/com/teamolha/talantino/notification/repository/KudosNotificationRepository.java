package com.teamolha.talantino.notification.repository;

import com.teamolha.talantino.notification.model.entity.KudosNotification;
import com.teamolha.talantino.talent.model.entity.Talent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface KudosNotificationRepository extends JpaRepository<KudosNotification, Long> {

    long countByReadFalseAndToTalentEmailIgnoreCase(String email);

    void deleteByToTalentId(Long talentId);

    void deleteByProofId(Long proofId);

    void deleteByFromSponsorId(Long sponsorId);

    long countByToTalentEmailIgnoreCase(String email);

    List<KudosNotification> findByToTalentEmailIgnoreCaseOrderByIdDesc(String email, Pageable pageable);

    void deleteByExpirationDateLessThanEqualAndReadTrue(Date expirationDate);
}
