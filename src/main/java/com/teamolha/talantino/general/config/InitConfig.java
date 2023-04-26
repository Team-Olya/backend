package com.teamolha.talantino.general.config;

import com.teamolha.talantino.sponsor.repository.SponsorRepository;
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

    private SponsorRepository sponsorRepository;

    @Override
    public void run(String... args) throws Exception {
        log.error(String.valueOf(Calendar.getInstance().getTime()));
        sponsorRepository.deleteAllByDeletionDateLessThanEqual(Calendar.getInstance().getTime());
    }
}
