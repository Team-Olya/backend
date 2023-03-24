package com.teamolha.talantino.service.impl;

import com.teamolha.talantino.model.response.TalentGeneralResponse;
import com.teamolha.talantino.model.response.TalentsPageResponse;
import com.teamolha.talantino.repository.TalentRepository;
import com.teamolha.talantino.service.TalentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TalentServiceImpl implements TalentService {
    TalentRepository talentRepository;

    public TalentsPageResponse pageTalents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<TalentGeneralResponse> talents = talentRepository.findAll(pageable).stream()
                .map(TalentGeneralResponse::new)
                .toList();
        return TalentsPageResponse.builder()
                .totalAmount(talentRepository.findAll().size())
                .talents(talents)
                .build();
    }
}

