package com.teamolha.talantino.sponsor.mapper;

import com.teamolha.talantino.general.config.Roles;
import com.teamolha.talantino.sponsor.model.entity.Sponsor;
import com.teamolha.talantino.sponsor.model.response.SponsorProfileResponse;
import org.mapstruct.Mapper;
import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Collectors;

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
}
