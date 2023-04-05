package com.teamolha.talantino.proof.service.impl;

import com.teamolha.talantino.proof.mapper.ProofMapper;
import com.teamolha.talantino.proof.model.Status;
import com.teamolha.talantino.proof.model.response.ProofDTO;
import com.teamolha.talantino.proof.model.response.ProofsPageDTO;
import com.teamolha.talantino.proof.model.response.TalentProofList;
import com.teamolha.talantino.proof.repository.ProofRepository;
import com.teamolha.talantino.proof.service.ProofService;
import com.teamolha.talantino.talent.repository.TalentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ProofServiceImpl implements ProofService {

    ProofMapper mapper;

    ProofRepository proofRepository;
    private final TalentRepository talentRepository;

    @Override
    public ProofsPageDTO pageProofs(String sort, int page, int count) {
        int totalAmount = proofRepository.findByStatus(Status.PUBLISHED.name()).size();

        if (count <= 0) {
            return ProofsPageDTO.builder()
                    .totalAmount(totalAmount)
                    .build();
        }

        Pageable pageable = sort.equals("desc") ?
                PageRequest.of(page, count, Sort.Direction.DESC, "date") :
                PageRequest.of(page, count, Sort.Direction.ASC, "date");

        List<ProofDTO> proofs = proofRepository.findByStatus(Status.PUBLISHED.name(), pageable)
                .stream().map(mapper::toProofDTO).toList();

        return ProofsPageDTO.builder()
                .totalAmount(totalAmount)
                .proofs(proofs)
                .build();
    }

    @Override
    public TalentProofList talentProofs(String name, String sort, String sortType, String status, Integer amount, Integer page, Long talentId) {
        if(talentRepository.findById(talentId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Talent with id " + talentId + " not found");
        };

        if (talentRepository.findByEmailIgnoreCase(name).orElseThrow().getId() != talentId && !status.equals(Status.PUBLISHED.name())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Other talents can see only PUBLISHED proofs");
        }

        Pageable pageable = sortType.equals("desc") ?
                PageRequest.of(page, amount, Sort.Direction.DESC, sort) :
                PageRequest.of(page, amount, Sort.Direction.ASC, sort);

        long totalAmount = status.equals("none") ?
                proofRepository.findByTalent_Id(talentId).size():
                proofRepository.findByStatusAndTalent_Id(status, talentId).size();

        var proofs = status.equals("none") ?
                proofRepository.findByTalent_Id(talentId, pageable)
                        .stream()
                        .map(mapper::toProofDTO)
                        .toList():
                proofRepository.findByStatusAndTalent_Id(status, talentId, pageable)
                        .stream()
                        .map(mapper::toProofDTO)
                        .toList()
                ;

        return TalentProofList.builder()
                .totalAmount(totalAmount)
                .proofs(proofs)
                .build();
    }

}
