package com.teamolha.talantino.talent.service;

import com.teamolha.talantino.talent.model.request.TalentUpdateRequest;
import com.teamolha.talantino.talent.model.response.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface TalentService {

    void register(String email, String password, String name, String surname, String kind, HttpServletRequest request);

    TalentsPageResponse pageTalents(int page, int size, String skills);

    TalentFullResponse talentProfile(long talentId);

    TalentProfileResponse talentProfile(String email);

    UpdatedTalentResponse updateTalentProfile(long talentId, String email, TalentUpdateRequest updateTalent);

    void deleteTalent(long talentId, String email);

    List<KindDTO> getTalentKinds();
}
