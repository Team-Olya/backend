package com.teamolha.talantino.service.impl;

import com.teamolha.talantino.mapper.Mappers;
import com.teamolha.talantino.model.entity.Link;
import com.teamolha.talantino.model.entity.Talent;
import com.teamolha.talantino.model.response.LinkResponse;
import com.teamolha.talantino.model.response.TalentGeneralResponse;
import com.teamolha.talantino.model.response.TalentProfileResponse;
import com.teamolha.talantino.model.response.TalentsPageResponse;
import com.teamolha.talantino.repository.LinkRepository;
import com.teamolha.talantino.repository.TalentRepository;
import com.teamolha.talantino.service.TalentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class TalentServiceImpl implements TalentService {

    Mappers mapper;

    LinkRepository linkRepository;

    TalentRepository talentRepository;

    public TalentsPageResponse pageTalents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<TalentGeneralResponse> talents = talentRepository.findAllByOrderByIdDesc(pageable).stream()
                .map(TalentGeneralResponse::new)
                .toList();
        return TalentsPageResponse.builder()
                .totalAmount(talentRepository.findAll().size())
                .talents(talents)
                .build();
    }

    @Override
    public TalentProfileResponse talentProfile(long talentId) {
        Optional<Talent> talentOptional = talentRepository.findById(talentId);
        if (talentOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Talent with ID " + talentId + " not found!");
        }

        List<LinkResponse> links = linkRepository.findByTalentId(talentId)
                .stream().map(mapper::toLinkResponse).toList();

        return mapper.toTalentProfileResponse(talentOptional.get(), links);
    }
}

