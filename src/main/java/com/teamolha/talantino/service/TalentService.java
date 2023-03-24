package com.teamolha.talantino.service;

import com.teamolha.talantino.model.response.TalentGeneralResponse;
import com.teamolha.talantino.model.response.TalentsPageResponse;
import com.teamolha.talantino.repository.TalentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TalentService {
    public TalentsPageResponse pageTalents(int page, int size);
}
