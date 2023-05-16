package com.teamolha.talantino.sponsor.service;

import com.teamolha.talantino.sponsor.model.request.AddKudosRequest;
import com.teamolha.talantino.sponsor.model.response.BalanceAddingDTO;
import com.teamolha.talantino.sponsor.model.response.SponsorKudos;
import com.teamolha.talantino.sponsor.model.response.SponsorProfileResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import com.teamolha.talantino.sponsor.model.request.SponsorUpdateRequest;
import com.teamolha.talantino.sponsor.model.response.UpdatedSponsorResponse;

import java.util.List;

public interface SponsorService {
    void register(String email, String password, String name, String surname, HttpServletRequest request);

    SponsorProfileResponse addKudos(Authentication auth, AddKudosRequest addKudosRequest);

    UpdatedSponsorResponse updateSponsorProfile(long sponsorId, String email, SponsorUpdateRequest updateSponsor);

    void deleteSponsor(HttpServletRequest request, Authentication auth, long sponsorId);

    void recoverSponsor(String token);

    List<BalanceAddingDTO> getBalanceAddingHistory(Authentication auth);

    List<SponsorKudos> getKudosHistory(Authentication auth);
}
