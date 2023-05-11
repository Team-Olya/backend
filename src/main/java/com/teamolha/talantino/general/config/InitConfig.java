package com.teamolha.talantino.general.config;

import com.teamolha.talantino.sponsor.repository.SponsorRepository;
import com.teamolha.talantino.talent.repository.TalentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Calendar;

@Slf4j
@Transactional
@Configuration
@AllArgsConstructor
public class InitConfig implements CommandLineRunner {

    private TalentRepository talentRepository;

    @Override
    public void run(String... args) throws Exception {
        talentRepository.deleteAllByVerificationExpireDate(Calendar.getInstance().getTime());
        sponsorRepository.deleteAllByDeletionDateLessThanEqual(Calendar.getInstance().getTime());
    }
}