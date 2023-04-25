package com.teamolha.talantino.sponsor.service;

import com.teamolha.talantino.sponsor.model.request.AddKudosRequest;
import com.teamolha.talantino.sponsor.model.response.SponsorProfileResponse;
import org.springframework.security.core.Authentication;

public interface SponsorService {
    void register(String email, String password, String name, String surname);

    SponsorProfileResponse addKudos(Authentication auth, AddKudosRequest addKudosRequest);
}
