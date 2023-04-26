package com.teamolha.talantino.general.email.utils;

import com.teamolha.talantino.general.config.EmailProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailUtil {

    private JavaMailSender emailSender;

    private EmailProperties emailProperties;

    public void sendMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailProperties.getEmail());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void deactivateAccount(HttpServletRequest request, String email, String deletionToken) {
        sendMessage(email, emailProperties.getDeactivateAccountSubject(),
                String.format(emailProperties.getDeactivateAccountText(), constructRecoveryTokenLink(request, deletionToken)));
    }

    private String constructRecoveryTokenLink(HttpServletRequest request, String deletionToken) {
        return request.getHeader("Referer")
                + emailProperties.getRecoverSponsorUrl() + "?token=" + deletionToken;
    }
}
