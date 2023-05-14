package com.teamolha.talantino.talent.mapper;

import com.teamolha.talantino.general.config.Roles;
import com.teamolha.talantino.proof.model.entity.Kudos;
import com.teamolha.talantino.skill.model.request.SkillDTO;
import com.teamolha.talantino.talent.model.entity.Link;
import com.teamolha.talantino.talent.model.entity.Talent;
import com.teamolha.talantino.talent.model.response.TalentFullResponse;
import com.teamolha.talantino.talent.model.response.TalentProfileResponse;
import com.teamolha.talantino.talent.model.response.UpdatedTalentResponse;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface TalentMapper {

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
                .role(Roles.TALENT.name())
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
                .balance(talent.getProofs().stream()
                        .flatMap(proof -> proof.getKudos().stream())
                        .mapToLong(Kudos::getAmount)
                        .sum())
                .skills(talent.getSkills().stream().map(SkillDTO::new).toList())
                .build();
    }

    default TalentFullResponse toTalentFullResponse(Talent talent, Long prevId, Long nextId) {
        return TalentFullResponse.builder()
                .role(Roles.TALENT.name())
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

//    default UserDetails toUserDetails(Talent talent) {
//        return User.withUsername(talent.getEmail())
//                .password(talent.getPassword())
//                .authorities(talent.getAuthorities().stream()
//                        .map(SimpleGrantedAuthority::new)
//                        .toList()
//                )
//                .disabled(AccountStatus.INACTIVE.equals(talent.getAccountStatus()))
//                .build();
//    }
//
//    default UserDetails toUserDetails(Admin admin) {
//        return User.withUsername(admin.getLogin())
//                .password(admin.getPassword())
//                .authorities(admin.getAuthorities().stream()
//                        .map(SimpleGrantedAuthority::new)
//                        .toList()
//                )
//                .build();
//    }
//
//    default UserDetails toUserDetails(Sponsor sponsor) {
//        return User.withUsername(sponsor.getEmail())
//                .password(sponsor.getPassword())
//                .authorities(sponsor.getAuthorities().stream()
//                        .map(SimpleGrantedAuthority::new)
//                        .toList()
//                )
//                .disabled(SponsorStatus.INACTIVE.equals(sponsor.getStatus()))
//                .build();
//    }
}
