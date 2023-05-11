package com.teamolha.talantino.talent.mapper;

import com.teamolha.talantino.admin.model.entity.Admin;
import com.teamolha.talantino.skill.model.entity.Skill;
import com.teamolha.talantino.skill.model.request.SkillDTO;
import com.teamolha.talantino.talent.model.entity.Link;
import com.teamolha.talantino.talent.model.entity.Talent;
import com.teamolha.talantino.talent.model.response.TalentProfileResponse;
import com.teamolha.talantino.talent.model.response.UpdatedTalentResponse;
import org.mapstruct.Mapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.stream.Collectors;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface Mappers {

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
                .skills(talent.getSkills().stream().map(SkillDTO::new).toList())
                .build();
    }

    default TalentProfileResponse toTalentProfileResponse(Talent talent, Long prevId, Long nextId) {
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
                .prevId(prevId)
                .nextId(nextId)
                .skills(talent.getSkills().stream().map(SkillDTO::new).toList())
                .build();
    }

    default UserDetails toUserDetails(Talent talent) {
        return User.withUsername(talent.getEmail())
                .password(talent.getPassword())
                .authorities(talent.getAuthorities().toArray(String[]::new))
                .build();
    }

    default UserDetails toUserDetails(Admin admin) {
        return User.withUsername(admin.getLogin())
                .password(admin.getPassword())
                .authorities(admin.getAuthorities().toArray(String[]::new))
                .build();
    }
}
