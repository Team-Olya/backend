package com.teamolha.talantino.proof.service.impl;

import com.teamolha.talantino.proof.mapper.ProofMapper;
import com.teamolha.talantino.proof.model.Status;
import com.teamolha.talantino.proof.model.entity.Proof;
import com.teamolha.talantino.proof.model.request.ProofRequest;
import com.teamolha.talantino.proof.model.response.*;
import com.teamolha.talantino.proof.repository.ProofRepository;
import com.teamolha.talantino.proof.service.ProofService;
import com.teamolha.talantino.talent.model.entity.Talent;
import com.teamolha.talantino.talent.repository.TalentRepository;
import com.teamolha.talantino.validation.ProofStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ProofServiceImpl implements ProofService {

    ProofMapper mapper;
    ProofRepository proofRepository;
    private final TalentRepository talentRepository;

    @Override
    public ProofsPageDTO pageProofs(Authentication auth, String sort, String type, int page, int count) {
        int totalAmount = proofRepository.findByStatus(Status.PUBLISHED.name()).size();

        if (count <= 0 || page < 0) return ProofsPageDTO.builder().totalAmount(totalAmount).build();

        Pageable pageable = type.equals("desc") ?
                PageRequest.of(page, count, Sort.Direction.DESC, sort) :
                PageRequest.of(page, count, Sort.Direction.ASC, sort);

        var talent = (auth == null) ? null :
                talentRepository.findByEmailIgnoreCase(auth.getName()).orElse(null);

        List<ShortProofDTO> proofs = proofRepository.findByStatus(Status.PUBLISHED.name(), pageable)
                .stream().map(proof -> mapper.toShortProofDTO(proof, talent)).toList();

        return ProofsPageDTO.builder()
                .totalAmount(totalAmount)
                .proofs(proofs)
                .build();
    }

    @Override
    public TalentProofList talentProofs(String name, String sort, String sortType, String status, Integer amount, Integer page, Long talentId) {
        if (talentRepository.findById(talentId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Talent with id " + talentId + " not found");
        }

        if (talentRepository.findByEmailIgnoreCase(name).orElseThrow().getId() != talentId && !status.equals(Status.PUBLISHED.name())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Other talents can see only PUBLISHED proofs");
        }

        Pageable pageable = sortType.equals("desc") ?
                PageRequest.of(page, amount, Sort.Direction.DESC, sort) :
                PageRequest.of(page, amount, Sort.Direction.ASC, sort);

        long totalAmount = status.equals("ALL") ?
                proofRepository.findByTalent_Id(talentId).size() :
                proofRepository.findByStatusAndTalent_Id(status, talentId).size();

        var talent = talentRepository.findByEmailIgnoreCase(name).orElse(null);

        var proofs = status.equals("ALL") ?
                proofRepository.findByTalent_Id(talentId, pageable)
                        .stream()
                        .map(proof -> mapper.toProofDTO(proof, talent))
                        .toList() :
                proofRepository.findByStatusAndTalent_Id(status, talentId, pageable)
                        .stream()
                        .map(proof -> mapper.toProofDTO(proof, talent))
                        .toList();

        return TalentProofList.builder()
                .totalAmount(totalAmount)
                .proofs(proofs)
                .build();
    }

    @Override
    public void createProof(String email, Long talentId, ProofRequest proof) {
        var talent = talentRepository.findById(talentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Talent with ID " + talentId + " not found!"));

        if (!email.equals(talent.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        LocalDateTime date = LocalDateTime.now(ZoneOffset.UTC);

        proofRepository.save(
                Proof.builder()
                        .title(proof.title())
                        .date(date)
                        .description(proof.description())
                        .talent(talent)
                        .status(proof.status())
                        .build()
        );
    }

    @Override
    public ProofDTO updateProof(String email, Long talentId, Long proofId, ProofRequest newProof) {
        var talent = getTalent(talentId);

        if (!email.equals(talent.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Proof proof = getProofEntity(proofId);

        if (!talentId.equals(proof.getTalent().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (!proof.getStatus().equals(Status.DRAFT.name()) && (!proof.getTitle().equals(newProof.title()) ||
                !proof.getDescription().equals(newProof.description()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "It is forbidden to edit the contents of a proof that is published or hidden");
        }
        if (!proof.getStatus().equals(Status.DRAFT.name()) && newProof.status().equals(Status.DRAFT.name())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You cannot edit the status of an already published or hidden proof back to a draft");
        }
        return editProof(proof, newProof);
    }

    @Override
    public void deleteProof(Long talentId, Long proofId, String email) {
        var talent = getTalent(talentId);

        if (!email.equals(talent.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Proof proof = getProofEntity(proofId);
        if (proof.getTalent().getId() == talent.getId()) {
            proofRepository.delete(proof);
        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    @Override
    public ProofDTO getProof(Long proofId) {
        Proof proof = getProofEntity(proofId);

        if (!proof.getStatus().equals(Status.PUBLISHED.name())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You can see only PUBLISHED proofs");
        }

        return mapper.toProofDTO(proof);
    }

    @Override
    public int getNumberOfKudos(Long proofId) {
        var proof = getProofEntity(proofId);
        return proof.getKudos().size();
    }

    @Override
    public void setKudos(Authentication auth, Long proofId) {
        var talent = talentRepository.findByEmailIgnoreCase(auth.getName()).get();
        var proof = getProofEntity(proofId);
        var kudosedProofs = talent.getKudosedProofs();

        if (proof.getTalent().equals(talent)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't put kudos on your own proof");
        }

        if (!proof.getStatus().equals(Status.PUBLISHED.name())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only put kudos in published proofs");
        }

        if (!kudosedProofs.contains(proof)) {
            kudosedProofs.add(proof);
            talent.setKudosedProofs(kudosedProofs);
            talentRepository.save(talent);
        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Proof has already been kudosed");
    }

    private ProofDTO editProof(Proof proof, ProofRequest newProof) {
        Optional.ofNullable(newProof.title()).ifPresent(proof::setTitle);
        Optional.ofNullable(newProof.description()).ifPresent(proof::setDescription);
        Optional.ofNullable(newProof.status()).ifPresent(proof::setStatus);
        proofRepository.save(proof);
        return mapper.toProofDTO(proof);
    }

    private Talent getTalent(Long talentId) {
        return talentRepository.findById(talentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Talent with ID " + talentId + " not found"));
    }

    private Proof getProofEntity(Long proofId) {
        return proofRepository.findById(proofId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Proof with ID " + proofId + " not found"));
    }
}
