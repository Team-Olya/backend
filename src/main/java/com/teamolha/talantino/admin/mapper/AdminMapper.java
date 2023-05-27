package com.teamolha.talantino.admin.mapper;

import com.teamolha.talantino.admin.model.entity.Admin;
import com.teamolha.talantino.admin.model.response.AdminProfile;
import com.teamolha.talantino.account.model.AccountRole;
import com.teamolha.talantino.admin.model.response.AdminProofDTO;
import com.teamolha.talantino.admin.model.response.AdminTalentDTO;
import com.teamolha.talantino.proof.model.entity.Proof;
import com.teamolha.talantino.talent.model.entity.Talent;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface AdminMapper {
    default AdminProfile toAdminProfile(Admin admin){
        return AdminProfile.builder()
                .role(AccountRole.ADMIN.name())
                .id(admin.getId())
                .surname(admin.getSurname())
                .name(admin.getName())
                .build();
    };

    default AdminTalentDTO toAdminTalentDTO(Talent talent) {
        return AdminTalentDTO.builder()
                .id(talent.getId())
                .name(talent.getName())
                .surname(talent.getSurname())
                .email(talent.getEmail())
                .kind(talent.getKind().getKind())
                .deletionDate(talent.getDeletionDate())
                .verificationExpireDate(talent.getVerificationExpireDate())
                .accountStatus(talent.getAccountStatus())
                .build();
    }

    default AdminProofDTO toAdminProofDTO(Proof proof) {
        return AdminProofDTO.builder()
                .proofId(proof.getId())
                .authorId(proof.getTalent().getId())
                .title(proof.getTitle())
                .description(proof.getDescription())
                .date(proof.getDate())
                .status(proof.getStatus())
                .build();
    }
}
