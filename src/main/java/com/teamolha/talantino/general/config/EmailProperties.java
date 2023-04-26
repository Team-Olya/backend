package com.teamolha.talantino.general.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Getter
@Configuration
public class EmailProperties {

    @Autowired
    private Environment env;

    @Value("${email.subject.deactivate.account}")
    private String deactivateAccountSubject;

    @Value("${email.text.deactivate.account}")
    private String deactivateAccountText;

    @Value("${email.recover.url}")
    private String recoverSponsorUrl;

    public String getEmail() {
        return env.getProperty("MAIL_USER");
    }
}
