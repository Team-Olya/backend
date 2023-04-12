package com.teamolha.talantino.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Slf4j
public class EnvVariables implements CommandLineRunner {

    Environment environment;

    @Override
    public void run(String... args) throws Exception {
        log.error("SPRING_PROFILES_ACTIVE={}", environment.getProperty("SPRING_PROFILES_ACTIVE"));
        log.error("DB_URL={}", environment.getProperty("DB_URL"));
        log.error("DB_LOGIN={}", environment.getProperty("DB_LOGIN"));
    }
}
