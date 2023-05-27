package com.teamolha.talantino.admin.model.response;

import com.teamolha.talantino.account.model.AccountStatus;
import lombok.Builder;

import java.util.Date;

@Builder
public record AdminSponsorDTO(
        Long id,
        String name,
        String surname,
        String avatar,
        Long balance,
        AccountStatus accountStatus,
        Date verificationExpireDate,
        Date deletionDate
) {
}
