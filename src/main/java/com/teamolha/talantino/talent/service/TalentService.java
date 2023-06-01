package com.teamolha.talantino.talent.service;

import com.teamolha.talantino.talent.model.request.TalentUpdateRequest;
import com.teamolha.talantino.talent.model.response.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface TalentService {

    void register(String email, String password, String name, String surname, String kind, HttpServletRequest request);

    TalentsPageResponse pageTalents(int page, int size, String skills);

    TalentFullResponse talentProfile(long talentId);

    TalentProfileResponse talentProfile(String email);

    UpdatedTalentResponse updateTalentProfile(long talentId, String email, TalentUpdateRequest updateTalent);

    void deleteTalent(HttpServletRequest request, long talentId, String email);

    KindsDTO getTalentKinds(int page, int size);

    TalentStatistic getStatistic(long talentId, String email);

    ReportedTalentDTO reportTalent(Authentication auth, Long talentId, HttpServletRequest request);

    void approveReport(Long reportId);

    void rejectReport(Long reportId);
}
