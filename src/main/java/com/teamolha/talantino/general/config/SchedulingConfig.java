package com.teamolha.talantino.general.config;

import com.teamolha.talantino.account.model.entity.Account;
import com.teamolha.talantino.account.repository.AccountRepository;
import com.teamolha.talantino.notification.model.entity.KudosNotification;
import com.teamolha.talantino.notification.repository.KudosNotificationRepository;
import com.teamolha.talantino.proof.repository.ProofRepository;
import com.teamolha.talantino.sponsor.repository.BalanceChangingRepository;
import com.teamolha.talantino.sponsor.repository.SponsorRepository;
import com.teamolha.talantino.talent.model.entity.Talent;
import com.teamolha.talantino.talent.repository.LinkRepository;
import com.teamolha.talantino.talent.repository.TalentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Transactional
@Configuration
@AllArgsConstructor
@EnableScheduling
public class SchedulingConfig {

    private TalentRepository talentRepository;

    private SponsorRepository sponsorRepository;

    private AccountRepository accountRepository;

    private KudosNotificationRepository notificationRepository;

    private LinkRepository linkRepository;

    private ProofRepository proofRepository;

    private BalanceChangingRepository balanceChangingRepository;

    @Scheduled(fixedRateString = "${deletion.expired.account.time}")
    public void deleteExpiredAccount() throws InterruptedException {
        Date curdate = Calendar.getInstance().getTime();
        log.info("Expired accounts deleted");
        log.info("Expired notifications deleted");
        cascadeDeletion(accountRepository.findByVerificationExpireDateLessThanEqual(curdate));
        cascadeDeletion(accountRepository.findByDeletionDateLessThanEqual(curdate));
        notificationRepository.deleteByExpirationDateLessThanEqualAndReadTrue(curdate);
    }

    private void cascadeDeletion(List<Account> accounts) {
        for (Account account : accounts) {
            if (account instanceof Talent) {
                linkRepository.deleteByTalentId(account.getId());
                proofRepository.deleteByTalentId(account.getId());
                var balanceChanging = balanceChangingRepository.findAllByTalent((Talent) account);
                balanceChanging.forEach(changing -> {
                    changing.setTalent(null);
                    balanceChangingRepository.save(changing);
                });
                notificationRepository.deleteByToTalentId(account.getId());
                talentRepository.deleteById(account.getId());
            } else {
                notificationRepository.deleteByFromSponsorId(account.getId());
                sponsorRepository.deleteById(account.getId());
            }
        }
    }
}