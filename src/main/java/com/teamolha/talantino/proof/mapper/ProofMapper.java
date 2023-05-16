package com.teamolha.talantino.proof.mapper;

import com.teamolha.talantino.account.model.entity.Account;
import com.teamolha.talantino.proof.model.entity.Kudos;
import com.teamolha.talantino.proof.model.entity.Proof;
import com.teamolha.talantino.proof.model.response.ProofDTO;
import com.teamolha.talantino.proof.model.response.ReportedProofDTO;
import com.teamolha.talantino.proof.model.response.ShortProofDTO;
import com.teamolha.talantino.skill.model.request.SkillDTO;
import com.teamolha.talantino.sponsor.model.entity.Sponsor;
import com.teamolha.talantino.talent.model.entity.Talent;
import org.mapstruct.Mapper;

import java.util.stream.Collectors;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ProofMapper {

    default ProofDTO toProofDTO(Proof proof) {
        return ProofDTO.builder()
                .id(proof.getId())
                .date(proof.getDate())
                .title(proof.getTitle())
                .description(proof.getDescription())
                .authorId(proof.getTalent().getId())
                .status(proof.getStatus())
                .skills(proof.getSkills().stream().map(SkillDTO::new).collect(Collectors.toList()))
                .totalKudos(proof.getKudos()
                        .stream()
                        .mapToInt(Kudos::getAmount)
                        .sum())
                .build();
    }

    default ProofDTO toProofDTO(Proof proof, Sponsor sponsor) {
        boolean isKudosed = false;
        Integer totalKudosFromSponsor = null;
        if (sponsor != null) {
            isKudosed = sponsor.getKudos().stream().anyMatch(kudos -> kudos.getProofId().equals(proof.getId()));
            totalKudosFromSponsor = sponsor.getKudos().stream()
                    .filter(kudos -> kudos.getProofId().equals(proof.getId())).findFirst()
                    .map(Kudos::getAmount).orElse(null);
        }
        return ProofDTO.builder()
                .id(proof.getId())
                .date(proof.getDate())
                .title(proof.getTitle())
                .description(proof.getDescription())
                .authorId(proof.getTalent().getId())
                .status(proof.getStatus())
                .totalKudos(proof.getKudos()
                        .stream()
                        .mapToInt(Kudos::getAmount)
                        .sum()
                )
                .skills(proof.getSkills().stream().map(SkillDTO::new).toList())
                .totalKudosFromSponsor(totalKudosFromSponsor)
                .isKudosed(isKudosed)
                .build();
    }

    default ShortProofDTO toShortProofDTO(Proof proof, Sponsor sponsor) {
        boolean isKudosed = false;
        Integer totalKudosFromSponsor = null;
        if (sponsor != null) {
            isKudosed = sponsor.getKudos().stream().anyMatch(kudos -> kudos.getProofId().equals(proof.getId()));
            totalKudosFromSponsor = sponsor.getKudos().stream()
                    .filter(kudos -> kudos.getProofId().equals(proof.getId())).findFirst()
                    .map(Kudos::getAmount).orElse(null);
        }
        return ShortProofDTO.builder()
                .id(proof.getId())
                .date(proof.getDate())
                .title(proof.getTitle())
                .description(proof.getDescription().length() > 200 ?
                        proof.getDescription().substring(0, 200) :
                        proof.getDescription())
                .skills(proof.getSkills().stream().map(SkillDTO::new).collect(Collectors.toList()))
                .totalKudos(proof.getKudos()
                        .stream()
                        .mapToInt(Kudos::getAmount)
                        .sum()
                )
                .totalKudosFromSponsor(totalKudosFromSponsor)
                .isKudosed(isKudosed)
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
}
