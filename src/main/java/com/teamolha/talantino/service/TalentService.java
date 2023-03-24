package com.teamolha.talantino.service;

import com.teamolha.talantino.model.entity.response.TalentGeneralResponse;
import com.teamolha.talantino.model.entity.response.TalentsPageResponse;
import com.teamolha.talantino.repository.TalentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TalentService {
    TalentRepository talentRepository;

    public TalentService(TalentRepository talentRepository){
        this.talentRepository = talentRepository;
    }

    public TalentsPageResponse pageTalents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<TalentGeneralResponse> talents = talentRepository.findAll(pageable).stream()
                .map(TalentGeneralResponse::new)
                .toList();
        return TalentsPageResponse.builder()
                .totalAmount(talents.size())
                .talents(talents)
                .build();
    }
}
