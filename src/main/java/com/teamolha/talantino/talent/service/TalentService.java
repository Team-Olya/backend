package com.teamolha.talantino.talent.service;

import com.teamolha.talantino.talent.model.request.TalentUpdateRequest;
import com.teamolha.talantino.talent.model.response.TalentProfileResponse;
import com.teamolha.talantino.talent.model.response.TalentsPageResponse;
import com.teamolha.talantino.talent.model.response.UpdatedTalentResponse;

public interface TalentService {
    TalentsPageResponse pageTalents(int page, int size);

    TalentProfileResponse talentProfile(long talentId);

    TalentProfileResponse talentProfile(String email);

    UpdatedTalentResponse updateTalentProfile(long talentId, String email, TalentUpdateRequest updateTalent);

    void deleteTalent(long talentId, String email);
}
