package com.teamolha.talantino.proof.mapper;

import com.teamolha.talantino.proof.model.entity.Proof;
import com.teamolha.talantino.proof.model.response.ProofDTO;
import com.teamolha.talantino.proof.model.response.ShortProofDTO;
import com.teamolha.talantino.skill.model.request.SkillDTO;
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
                .build();
    }

    default ShortProofDTO toShortProofDTO(Proof proof) {
        return ShortProofDTO.builder()
                .id(proof.getId())
                .date(proof.getDate())
                .title(proof.getTitle())
                .description(proof.getDescription().length() > 200 ?
                        proof.getDescription().substring(0, 200) :
                        proof.getDescription())
                .skills(proof.getSkills().stream().map(SkillDTO::new).collect(Collectors.toList()))
                .build();
    }
}
