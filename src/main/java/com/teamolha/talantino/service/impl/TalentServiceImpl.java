package com.teamolha.talantino.service.impl;

import com.teamolha.talantino.mapper.Mappers;
import com.teamolha.talantino.model.entity.Kind;
import com.teamolha.talantino.model.entity.Link;
import com.teamolha.talantino.model.entity.Talent;
import com.teamolha.talantino.model.request.TalentUpdateRequest;
import com.teamolha.talantino.model.response.TalentGeneralResponse;
import com.teamolha.talantino.model.response.TalentProfileResponse;
import com.teamolha.talantino.model.response.TalentsPageResponse;
import com.teamolha.talantino.model.response.UpdatedTalentResponse;
import com.teamolha.talantino.repository.KindRepository;
import com.teamolha.talantino.repository.LinkRepository;
import com.teamolha.talantino.repository.TalentRepository;
import com.teamolha.talantino.service.TalentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
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

    @Override
    public TalentProfileResponse talentProfile(String email) {
        return mapper.toTalentProfileResponse(
                talentRepository.findByEmailIgnoreCase(email).get()
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
        Optional<Talent> talentOptional = talentRepository.findById(talentId);
        if (talentOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Talent with ID " + talentId + " not found!");
        }

        return mapper.toTalentProfileResponse(talentOptional.get());
    }

    @Override
    public UpdatedTalentResponse updateTalentProfile(long talentId, String email, TalentUpdateRequest updateTalent) {
        Optional<Talent> talentOptional = talentRepository.findById(talentId);
        if(talentOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Talent with ID " + talentId + " not found!");
        }
        var talent = talentOptional.get();
        if(!talent.getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return updateTalent(talent, updateTalent);
    }

    @Override
    public void deleteTalent(long talentId, String email) {
        Optional<Talent> talentOptional = talentRepository.findById(talentId);
        if (talentOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Talent with ID " + talentId + " not found");
        }

        Talent talent = talentOptional.get();
        if (!email.equals(talent.getEmail())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        linkRepository.deleteByTalent(talent);
        talentRepository.delete(talent);
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

