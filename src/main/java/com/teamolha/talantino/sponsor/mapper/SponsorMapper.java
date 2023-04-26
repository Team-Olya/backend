package com.teamolha.talantino.sponsor.mapper;

import com.teamolha.talantino.general.config.Roles;
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
                .role(Roles.SPONSOR.name())
                .id(sponsor.getId())
                .name(sponsor.getName())
                .surname(sponsor.getSurname())
                .avatar(sponsor.getAvatar())
                .balance(sponsor.getBalance())
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
