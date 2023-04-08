package com.teamolha.talantino.talent.service.impl;

import com.teamolha.talantino.proof.repository.ProofRepository;
import com.teamolha.talantino.talent.mapper.Mappers;
import com.teamolha.talantino.talent.model.entity.Kind;
import com.teamolha.talantino.talent.model.entity.Link;
import com.teamolha.talantino.talent.model.entity.Talent;
import com.teamolha.talantino.talent.model.request.TalentUpdateRequest;
import com.teamolha.talantino.talent.model.response.TalentGeneralResponse;
import com.teamolha.talantino.talent.model.response.TalentProfileResponse;
import com.teamolha.talantino.talent.model.response.TalentsPageResponse;
import com.teamolha.talantino.talent.model.response.UpdatedTalentResponse;
import com.teamolha.talantino.talent.repository.KindRepository;
import com.teamolha.talantino.talent.repository.LinkRepository;
import com.teamolha.talantino.talent.repository.TalentRepository;
import com.teamolha.talantino.talent.service.TalentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class TalentServiceImpl implements TalentService {

    Mappers mapper;

    LinkRepository linkRepository;

    TalentRepository talentRepository;

    KindRepository kindRepository;

    ProofRepository proofRepository;

    @Override
    public TalentProfileResponse talentProfile(String email) {
        var talent = talentRepository.findByEmailIgnoreCase(email).get();

        return mapper.toTalentProfileResponse(
                talent, getPrevTalentId(talent.getId()), getNextTalentId(talent.getId())
        );
    }

    @Transactional(readOnly = true)
    @Override
    public TalentsPageResponse pageTalents(int page, int size) {
        if (size <= 0) {
            return TalentsPageResponse.builder()
                    .totalAmount(talentRepository.findAll().size())
                    .build();
        }
        Pageable pageable = PageRequest.of(page, size);
        List<TalentGeneralResponse> talents = talentRepository.findAllByOrderByIdDesc(pageable).stream()
                .map(TalentGeneralResponse::new)
                .toList();
        return TalentsPageResponse.builder()
                .totalAmount(talentRepository.findAll().size())
                .talents(talents)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public TalentProfileResponse talentProfile(long talentId) {
        var talent = talentRepository.findById(talentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Talent with ID " + talentId + " not found!"));

        return mapper.toTalentProfileResponse(talent, getPrevTalentId(talentId), getNextTalentId(talentId));
    }

    @Override
    public UpdatedTalentResponse updateTalentProfile(long talentId, String email, TalentUpdateRequest updateTalent) {
        var talent = talentRepository.findById(talentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Talent with ID " + talentId + " not found!"));

        if (!talent.getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return updateTalent(talent, updateTalent);
    }

    @Override
    public void deleteTalent(long talentId, String email) {
        var talent = talentRepository.findById(talentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Talent with ID " + talentId + " not found"));

        if (!email.equals(talent.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        linkRepository.deleteByTalent(talent);
        proofRepository.deleteByTalent(talent);
        talentRepository.delete(talent);
    }

    private Long getPrevTalentId(long talentId) {
        return talentRepository.findPrevTalent(talentId);
    }

    private Long getNextTalentId(long talentId) {
        return talentRepository.findNextTalent(talentId);
    }

    private UpdatedTalentResponse updateTalent(Talent oldTalent, TalentUpdateRequest newTalent) {
        oldTalent.setName(newTalent.name());
        oldTalent.setSurname(newTalent.surname());
        if (!kindRepository.existsByKindIgnoreCase(newTalent.kind())) {
            kindRepository.save(
                    Kind.builder()
                            .kind(newTalent.kind())
                            .build()
            );
        }
        oldTalent.setKind(kindRepository.findByKindIgnoreCase(newTalent.kind()));
        oldTalent.setAvatar(newTalent.avatar());
        oldTalent.setDescription(newTalent.description());
        oldTalent.setExperience(newTalent.experience());
        oldTalent.setLocation(newTalent.location());
        linkRepository.deleteByTalent(oldTalent);
        List<Link> links = newTalent.links().stream().map(url -> linkRepository.save(
                Link.builder()
                        .url(url)
                        .talent(oldTalent)
                        .build()
        )).collect(Collectors.toList());
        oldTalent.setLinks(links);
        talentRepository.save(oldTalent);
        return mapper.toUpdatedTalent(oldTalent);
    }
}

