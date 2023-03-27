package com.teamolha.talantino.mapper;

import com.teamolha.talantino.model.entity.Link;
import com.teamolha.talantino.model.entity.Talent;
import com.teamolha.talantino.model.response.LinkResponse;
import com.teamolha.talantino.model.response.TalentProfileResponse;
import com.teamolha.talantino.model.response.UpdatedTalentResponse;
import org.mapstruct.Mapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface Mappers {

    LinkResponse toLinkResponse(Link link);

    default UpdatedTalentResponse toUpdatedTalent(Talent talent) {
        return UpdatedTalentResponse.builder()
                .id(talent.getId())
                .name(talent.getName())
                .surname(talent.getSurname())
                .kind(talent.getKind().getKind())
                .description(talent.getDescription())
                .avatar(talent.getAvatar())
                .experience(talent.getExperience())
                .location(talent.getLocation())
                .links(talent
                        .getLinks()
                        .stream()
                        .map(Link::getUrl)
                        .toList())
                .build();
    }

    default TalentProfileResponse toTalentProfileResponse(Talent talent) {
        return TalentProfileResponse.builder()
                .id(talent.getId())
                .name(talent.getName())
                .surname(talent.getSurname())
                .email(talent.getEmail())
                .kind(talent.getKind().getKind())
                .description(talent.getDescription())
                .avatar(talent.getAvatar())
                .experience(talent.getExperience())
                .location(talent.getLocation())
                .links(talent
                        .getLinks()
                        .stream()
                        .map(Link::getUrl)
                        .toList()
                )
                .build();
    }

    default UserDetails toUserDetails(Talent talent) {
        return User.withUsername(talent.getEmail())
                .password(talent.getPassword())
                .authorities(talent.getAuthorities().toArray(String[]::new))
                .build();
    }
}
