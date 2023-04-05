package com.teamolha.talantino.proof.mapper;

import com.teamolha.talantino.proof.model.entity.Proof;
import com.teamolha.talantino.proof.model.response.ProofDTO;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ProofMapper {

    ProofDTO toProofDTO(Proof proof);
}
