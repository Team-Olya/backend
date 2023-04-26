package com.teamolha.talantino.sponsor.service;

import com.teamolha.talantino.sponsor.model.request.SponsorUpdateRequest;
import com.teamolha.talantino.sponsor.model.response.UpdatedSponsorResponse;

public interface SponsorService {
    void register(String email, String password, String name, String surname);

    UpdatedSponsorResponse updateSponsorProfile(long sponsorId, String email, SponsorUpdateRequest updateSponsor);
}
