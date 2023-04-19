package com.teamolha.talantino.proof.mapper;

import com.teamolha.talantino.proof.model.entity.Kudos;
import com.teamolha.talantino.proof.model.entity.Proof;
import com.teamolha.talantino.proof.model.response.ProofDTO;
import com.teamolha.talantino.proof.model.response.ShortProofDTO;
import com.teamolha.talantino.talent.model.entity.Talent;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;

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
                .build();
    }

    default ProofDTO toProofDTO(Proof proof, Talent talent) {
        return ProofDTO.builder()
                .id(proof.getId())
                .date(proof.getDate())
                .title(proof.getTitle())
                .description(proof.getDescription())
                .authorId(proof.getTalent().getId())
                .status(proof.getStatus())
                .totalKudos(proof.getKudos().size())
                .isKudosed(false) // TODO:Влад Только для спонсора
                .build();
    }

    default ShortProofDTO toShortProofDTO(Proof proof, Talent talent) {
        boolean isKudosed = false, isAuthor = false;
        if (talent != null) {
            isKudosed = false; // TODO:Влад Только для спонсора
            isAuthor = talent.getProofs().contains(proof);
        }

        return ShortProofDTO.builder()
                .id(proof.getId())
                .date(proof.getDate())
                .title(proof.getTitle())
                .description(proof.getDescription().length() > 200 ?
                        proof.getDescription().substring(0, 200) :
                        proof.getDescription())
                .totalKudos(proof.getKudos()
                        .stream()
                        .mapToInt(Kudos::getAmount)
                        .sum()
                ) // TODO:Влад теперь считает сумму кудосов, а не их количество
                .isKudosed(isKudosed)
                .isAuthor(isAuthor)
                .build();
    }
}
