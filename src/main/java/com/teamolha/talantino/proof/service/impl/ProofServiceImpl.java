package com.teamolha.talantino.proof.service.impl;

import com.teamolha.talantino.proof.mapper.ProofMapper;
import com.teamolha.talantino.proof.model.Status;
import com.teamolha.talantino.proof.model.entity.Kudos;
import com.teamolha.talantino.proof.model.entity.Proof;
import com.teamolha.talantino.proof.model.response.KudosDTO;
import com.teamolha.talantino.proof.model.request.ProofRequest;
import com.teamolha.talantino.proof.model.response.*;
import com.teamolha.talantino.proof.repository.KudosRepository;
import com.teamolha.talantino.proof.repository.ProofRepository;
import com.teamolha.talantino.proof.service.ProofService;
import com.teamolha.talantino.sponsor.repository.SponsorRepository;
import com.teamolha.talantino.sponsor.mapper.SponsorMapper;
import com.teamolha.talantino.sponsor.repository.SponsorRepository;
import com.teamolha.talantino.sponsor.mapper.SponsorMapper;
import com.teamolha.talantino.talent.model.entity.Talent;
import com.teamolha.talantino.talent.repository.TalentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class ProofServiceImpl implements ProofService {

    ProofMapper mapper;
    ProofRepository proofRepository;
    SponsorMapper sponsorMapper;
    TalentRepository talentRepository;
    SponsorRepository sponsorRepository;
    KudosRepository kudosRepository;

    @Transactional(readOnly = true)
    @Override
    public ProofsPageDTO pageProofs(Authentication auth, String sort, String type, int page, int count) {
        int totalAmount = proofRepository.findByStatus(Status.PUBLISHED.name()).size();

        if (count <= 0 || page < 0) return ProofsPageDTO.builder().totalAmount(totalAmount).build();

        Pageable pageable = type.equals("desc") ?
                PageRequest.of(page, count, Sort.Direction.DESC, sort) :
                PageRequest.of(page, count, Sort.Direction.ASC, sort);

        var sponsor = (auth == null) ? null :
                sponsorRepository.findByEmailIgnoreCase(auth.getName()).orElse(null);

        List<ShortProofDTO> proofs = proofRepository.findByStatus(Status.PUBLISHED.name(), pageable)
                .stream().map(proof -> mapper.toShortProofDTO(proof, sponsor)).toList();

        return ProofsPageDTO.builder()
                .totalAmount(totalAmount)
                .proofs(proofs)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public TalentProofList talentProofs(Authentication auth, String sort, String sortType, String status, Integer amount, Integer page, Long talentId) {
        if (talentRepository.findById(talentId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Talent with id " + talentId + " not found");
        }

        if ((isTalent(auth) && talentRepository.findByEmailIgnoreCase(auth.getName()).get().getId() != talentId ||
                isSponsor(auth)) && !status.equals(Status.PUBLISHED.name())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You can see only PUBLISHED proofs");
        }

        Pageable pageable = sortType.equals("desc") ?
                PageRequest.of(page, amount, Sort.Direction.DESC, sort) :
                PageRequest.of(page, amount, Sort.Direction.ASC, sort);

        long totalAmount = status.equals("ALL") ?
                proofRepository.findByTalent_Id(talentId).size() :
                proofRepository.findByStatusAndTalent_Id(status, talentId).size();

        var talent = talentRepository.findByEmailIgnoreCase(auth.getName()).orElse(null);

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

    @Transactional(readOnly = true)
    @Override
    public ProofDTO getProof(Long proofId) {
        Proof proof = getProofEntity(proofId);

        if (!proof.getStatus().equals(Status.PUBLISHED.name())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You can see only PUBLISHED proofs");
        }

        return mapper.toProofDTO(proof);
    }

    @Transactional(readOnly = true)
    @Override
    public KudosList getKudos(Authentication auth, Long proofId) {
        var proof = getProofEntity(proofId);
        var talent = (auth == null) ? null :
                talentRepository.findByEmailIgnoreCase(auth.getName()).orElse(null);
        List<KudosDTO> kudos = new ArrayList<>();
        if(proof.getTalent().equals(talent)){
            kudos = getKudos(proofId);
        }
        return KudosList.builder()
                .totalAmount(proof.getKudos().size())
                .kudos(kudos).build();
    }

    @Override
    public void setKudos(Authentication auth, Long proofId, int amount) {
        var sponsor = sponsorRepository.findByEmailIgnoreCase(auth.getName()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.FORBIDDEN, "Only sponsors have access to kudos"));
        if (amount <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must bet at least 1 kudos");
        }
        if (sponsor.getBalance() < amount) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Not enough balance");
        }

        var proof = getProofEntity(proofId);
        if (!proof.getStatus().equals(Status.PUBLISHED.name())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only put kudos in published proofs");
        }

        List<Kudos> sponsorKudos = sponsor.getKudos();
        if (!kudosRepository.existsByProofId(proofId)) {
            sponsorKudos.add(Kudos.builder()
                    .amount(amount)
                    .sponsorId(sponsor.getId())
                    .proofId(proofId)
                    .build());
        } else {
            sponsorKudos.stream().filter(kudos -> kudos.getProofId().equals(proofId))
                    .forEach(kudos -> kudos.setAmount(kudos.getAmount() + amount));
        }
        sponsor.setKudos(sponsorKudos);
        sponsor.setBalance(sponsor.getBalance() - amount);
        sponsorRepository.save(sponsor);
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

    private List<KudosDTO> getKudos(Long proofId){
        List<KudosDTO> kudos = new ArrayList<>();
        List<Object[]> list = proofRepository.findSponsorsAndKudosOnProof(proofId);
        for (Object[] elem : list) {
            long sponsorId = ((Number) elem[0]).longValue();
            int amount = ((Number) elem[1]).intValue();
            sponsorRepository.findById(sponsorId).ifPresent(
                    sponsor -> kudos.add(KudosDTO.builder()
                            .sponsor(sponsorMapper.toShortSponsorDTO(sponsor))
                            .amountOfKudos(amount)
                            .build())
            );
        }
        return kudos;
    }

    private boolean isTalent(Authentication auth) {
        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().contains(Roles.TALENT.name());
    }

    private boolean isSponsor(Authentication auth) {
        return auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().contains(Roles.SPONSOR.name());
    }
}
