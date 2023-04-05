package com.teamolha.talantino.proof.service.impl;

import com.teamolha.talantino.proof.mapper.ProofMapper;
import com.teamolha.talantino.proof.model.response.ProofDTO;
import com.teamolha.talantino.proof.model.response.ProofsPageDTO;
import com.teamolha.talantino.proof.repository.ProofRepository;
import com.teamolha.talantino.proof.service.ProofService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProofServiceImpl implements ProofService {

    ProofMapper mapper;

    ProofRepository proofRepository;

    @Override
    public ProofsPageDTO pageProofs(String sort, int page, int count) {
        int totalAmount = proofRepository.findAll().size();

        if (count <= 0) {
            return ProofsPageDTO.builder()
                    .totalAmount(totalAmount)
                    .build();
        }

        Pageable pageable = sort.equals("desc") ?
                PageRequest.of(page, count, Sort.Direction.DESC, "date") :
                PageRequest.of(page, count, Sort.Direction.ASC, "date");

        List<ProofDTO> proofs = proofRepository.findAll(pageable)
                .stream().map(mapper::toProofDTO).toList();

        return ProofsPageDTO.builder()
                .totalAmount(totalAmount)
                .proofs(proofs)
                .build();
    }
}
