package com.teamolha.talantino.talent.mapper;

import com.teamolha.talantino.account.model.AccountRole;
import com.teamolha.talantino.account.model.entity.Account;
import com.teamolha.talantino.proof.model.entity.Kudos;
import com.teamolha.talantino.proof.model.entity.Proof;
import com.teamolha.talantino.proof.model.response.ReportedProofDTO;
import com.teamolha.talantino.skill.model.request.SkillDTO;
import com.teamolha.talantino.talent.model.entity.Link;
import com.teamolha.talantino.talent.model.entity.Talent;
import com.teamolha.talantino.talent.model.response.ReportedTalentDTO;
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
                .role(AccountRole.TALENT.name())
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
                .role(AccountRole.TALENT.name())
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

    default ReportedTalentDTO toReportTalentDTO(Long reportId, Talent talent, Account account) {
        return ReportedTalentDTO.builder()
                .id(reportId)
                .fullName(talent.getName() + " " + talent.getSurname())
                .kind(talent.getKind().getKind())
                .description(talent.getDescription())
                .email(talent.getEmail())
                .avatar(talent.getAvatar())
                .reportedTalentId(talent.getId())
                .location(talent.getLocation())
                .reportedBy(account.getName() + " " + account.getSurname())
                .build();
    }
}
