package com.teamolha.talantino.admin.model.response;

import com.teamolha.talantino.account.model.AccountStatus;
import com.teamolha.talantino.proof.model.Status;
import com.teamolha.talantino.skill.model.request.SkillDTO;
import lombok.Builder;

import java.util.Date;

@Builder
public record AdminTalentDTO(
        Long id,
        String name,
        String surname,
        String kind,
        String email,
        AccountStatus accountStatus,
        Date verificationExpireDate,
        Date deletionDate
) {
}
