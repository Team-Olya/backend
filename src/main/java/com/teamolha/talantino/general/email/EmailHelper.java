package com.teamolha.talantino.general.email;

import com.teamolha.talantino.general.config.EmailProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EmailHelper {

    private EmailProperties emailProperties;

    public Date calculateExpireVerificationDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.HOUR_OF_DAY, emailProperties.getExpireVerification());
        return new Date(cal.getTime().getTime());
    }

    public Date calculateDeletionDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.DAY_OF_WEEK, emailProperties.getExpireDeletion());
        return new Date(cal.getTime().getTime());
    }

    public String generateUUIDToken() {
        return UUID.randomUUID().toString();
    }
}
