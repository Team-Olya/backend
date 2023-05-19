package com.teamolha.talantino.skill.mapper;

import com.teamolha.talantino.proof.model.entity.Kudos;
import com.teamolha.talantino.proof.model.entity.Proof;
import com.teamolha.talantino.skill.model.entity.Skill;
import com.teamolha.talantino.skill.model.request.ProofSkillDTO;
import com.teamolha.talantino.skill.model.request.SkillDTO;
import com.teamolha.talantino.sponsor.model.entity.Sponsor;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;


@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface SkillMapper {

    default SkillDTO toSkillDTO(Skill skill) {
        return SkillDTO.builder()
                .id(skill.getId())
                .label(skill.getLabel())
                .icon(skill.getIcon())
                .build();
    }

    default ProofSkillDTO toProofSkillDTO(Skill skill, Proof proof) {
        var totalKudos = proof.getKudos()
                .stream()
                .filter(kudos -> kudos.getSkillId() != null && kudos.getSkillId().equals(skill.getId()))
                .mapToInt(Kudos::getAmount)
                .sum();
        return ProofSkillDTO.builder()
                .id(skill.getId())
                .label(skill.getLabel())
                .icon(skill.getIcon())
                .totalKudos(totalKudos)
                .build();
    }

    default ProofSkillDTO toProofSkillDTO(Skill skill, Sponsor sponsor, Proof proof) {
        boolean isKudosed = false;
        Integer totalKudosFromSponsor = null;
        var totalKudos = proof.getKudos()
                .stream()
                .filter(kudos -> kudos.getSkillId() != null && kudos.getSkillId().equals(skill.getId()))
                .mapToInt(Kudos::getAmount)
                .sum();
        if (sponsor != null) {
            isKudosed = sponsor.getKudos()
                    .stream()
                    .anyMatch(kudos -> kudos.getProofId().equals(proof.getId()) &&
                            kudos.getSkillId().equals(skill.getId())
                    );
            totalKudosFromSponsor = sponsor.getKudos().stream()
                    .filter(kudos -> kudos.getProofId().equals(proof.getId()) &&
                            kudos.getSkillId().equals(skill.getId())).findFirst()
                    .map(Kudos::getAmount).orElse(null);
        }
        return ProofSkillDTO.builder()
                .id(skill.getId())
                .label(skill.getLabel())
                .icon(skill.getIcon())
                .isKudosed(isKudosed)
                .totalKudosFromSponsor(totalKudosFromSponsor)
                .totalKudos(totalKudos)
                .build();
    }

}
