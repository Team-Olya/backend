package com.teamolha.talantino.sponsor.mapper;

import com.teamolha.talantino.account.model.AccountRole;
import com.teamolha.talantino.proof.model.entity.Kudos;
import com.teamolha.talantino.sponsor.model.entity.Sponsor;
import com.teamolha.talantino.sponsor.model.response.ShortSponsorDTO;
import com.teamolha.talantino.sponsor.model.response.SponsorProfileResponse;
import com.teamolha.talantino.sponsor.model.response.UpdatedSponsorResponse;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface SponsorMapper {
    default SponsorProfileResponse toSponsorProfileResponse(Sponsor sponsor) {
        return SponsorProfileResponse.builder()
                .role(AccountRole.SPONSOR.name())
                .id(sponsor.getId())
                .name(sponsor.getName())
                .surname(sponsor.getSurname())
                .avatar(sponsor.getAvatar())
                .balance(sponsor.getBalance())
                .totalKudosed((long) sponsor.getKudos()
                        .size()
                )
                .totalSpent((long) sponsor.getKudos()
                        .stream()
                        .mapToInt(Kudos::getAmount)
                        .sum()
                )
                .build();
    }

    default UpdatedSponsorResponse toUpdatedSponsor(Sponsor sponsor) {
        return UpdatedSponsorResponse.builder()
                .id(sponsor.getId())
                .name(sponsor.getName())
                .surname(sponsor.getSurname())
                .balance(sponsor.getBalance())
                .avatar(sponsor.getAvatar())
                .build();
    }

    default ShortSponsorDTO toShortSponsorDTO(Sponsor sponsor) {
        return ShortSponsorDTO.builder()
                .name(sponsor.getName())
                .surname(sponsor.getSurname())
                .avatar(sponsor.getAvatar())
                .build();
    }
}
