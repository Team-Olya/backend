package com.teamolha.talantino.proof.mapper;

import com.teamolha.talantino.account.model.entity.Account;
import com.teamolha.talantino.proof.model.entity.Kudos;
import com.teamolha.talantino.proof.model.entity.Proof;
import com.teamolha.talantino.proof.model.response.ProofDTO;
import com.teamolha.talantino.proof.model.response.ReportedProofDTO;
import com.teamolha.talantino.proof.model.response.ShortProofDTO;
import com.teamolha.talantino.proof.repository.KudosRepository;
import com.teamolha.talantino.skill.mapper.SkillMapper;
import com.teamolha.talantino.sponsor.model.entity.Sponsor;
import com.teamolha.talantino.talent.model.entity.Talent;
import com.teamolha.talantino.talent.model.response.ProofAuthorDTO;
import org.mapstruct.Mapper;

import java.util.stream.Collectors;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ProofMapper {

    default ProofDTO toProofDTO(Proof proof, SkillMapper skillMapper, boolean isAuthenticated) {
        var talent = proof.getTalent();
        var author = getAuthor(talent, isAuthenticated);
        return ProofDTO.builder()
                .id(proof.getId())
                .date(proof.getDate())
                .title(proof.getTitle())
                .description(proof.getDescription())
                .author(author)
                .status(proof.getStatus())
                .totalKudos(proof.getKudos().stream().mapToInt(Kudos::getAmount).sum())
                .skills(proof.getSkills().stream().map(skill -> skillMapper.toProofSkillDTO(skill, proof)).collect(Collectors.toList()))
                .build();
    }

    default ProofDTO toProofDTO(Proof proof, Sponsor sponsor, SkillMapper skillMapper, boolean isAuthenticated) {
        boolean isKudosed = false;
        Integer totalKudosFromSponsor = null;
        var talent = proof.getTalent();
        var author = getAuthor(talent, isAuthenticated);
        if (sponsor != null) {
            isKudosed = sponsor.getKudos().stream().anyMatch(kudos -> kudos.getProofId().equals(proof.getId()));
            totalKudosFromSponsor = sponsor.getKudos().stream().filter(kudos ->
                    kudos.getProofId().equals(proof.getId())).mapToInt(Kudos::getAmount).sum();
        }
        return ProofDTO.builder()
                .id(proof.getId())
                .date(proof.getDate())
                .title(proof.getTitle())
                .description(proof.getDescription())
                .author(author)
                .status(proof.getStatus())
                .totalKudos(proof.getKudos().stream().mapToInt(Kudos::getAmount).sum())
                .totalKudosFromSponsor(totalKudosFromSponsor)
                .isKudosed(isKudosed)
                .skills(proof.getSkills().stream().map(skill -> skillMapper.toProofSkillDTO(skill, sponsor, proof)).toList())
                .build();
    }

    default ShortProofDTO toShortProofDTO(Proof proof, Sponsor sponsor, SkillMapper skillMapper, boolean isAuthenticated) {
        boolean isKudosed = false;
        Integer totalKudosFromSponsor = null;
        if (sponsor != null) {
            isKudosed = sponsor.getKudos().stream().anyMatch(kudos -> kudos.getProofId().equals(proof.getId()));
            totalKudosFromSponsor = sponsor.getKudos().stream().filter(kudos ->
                    kudos.getProofId().equals(proof.getId())).mapToInt(Kudos::getAmount).sum();
        }
        var talent = proof.getTalent();
        var author = getAuthor(talent, isAuthenticated);
        return ShortProofDTO.builder()
                .id(proof.getId())
                .date(proof.getDate())
                .title(proof.getTitle())
                .description(proof.getDescription().length() > 200 ?
                        proof.getDescription().substring(0, 200) :
                        proof.getDescription())
                .author(author)
                .totalKudos(proof.getKudos().stream().mapToInt(Kudos::getAmount).sum())
                .totalKudosFromSponsor(totalKudosFromSponsor)
                .isKudosed(isKudosed)
                .skills(proof.getSkills().stream().map(skill -> skillMapper.toProofSkillDTO(skill, sponsor, proof)).collect(Collectors.toList()))
                .build();
    }

    default ReportedProofDTO toReportDTO(Long reportId, Proof proof, Account account) {
        Talent talent = proof.getTalent();

        return ReportedProofDTO.builder()
                .id(reportId)
                .title(proof.getTitle())
                .date(proof.getDate())
                .description(proof.getDescription())
                .proofAuthorId(talent.getId())
                .proofAuthor(talent.getName() + " " + talent.getSurname())
                .proofAuthorAvatar(talent.getAvatar())
                .reportedBy(account.getName() + " " + account.getSurname())
                .build();
    }

    private ProofAuthorDTO getAuthor(Talent talent, boolean isAuthenticated) {
        return isAuthenticated ? ProofAuthorDTO.builder()
                .id(talent.getId())
                .name(talent.getName())
                .surname(talent.getSurname())
                .avatar(talent.getAvatar())
                .build() : null;
    }
}
