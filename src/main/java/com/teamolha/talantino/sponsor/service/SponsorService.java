package com.teamolha.talantino.sponsor.service;

import com.teamolha.talantino.sponsor.model.request.AddKudosRequest;
import com.teamolha.talantino.sponsor.model.response.BalanceHistoryDTO;
import com.teamolha.talantino.sponsor.model.response.SponsorProfileResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import com.teamolha.talantino.sponsor.model.request.SponsorUpdateRequest;
import com.teamolha.talantino.sponsor.model.response.UpdatedSponsorResponse;

public interface SponsorService {
    void register(String email, String password, String name, String surname, HttpServletRequest request);

    SponsorProfileResponse addKudos(Authentication auth, AddKudosRequest addKudosRequest);

    UpdatedSponsorResponse updateSponsorProfile(long sponsorId, String email, SponsorUpdateRequest updateSponsor);

    void deleteSponsor(HttpServletRequest request, Authentication auth, long sponsorId);

    BalanceHistoryDTO getBalanceHistory(Authentication auth, int page, int size);
}
