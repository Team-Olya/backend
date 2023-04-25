package com.teamolha.talantino.sponsor.controller;

import com.teamolha.talantino.sponsor.model.request.AddKudosRequest;
import com.teamolha.talantino.sponsor.model.response.SponsorProfileResponse;
import com.teamolha.talantino.sponsor.service.SponsorService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SponsorController {

    private SponsorService sponsorService;

    @PostMapping("/sponsors/balance")
    public SponsorProfileResponse addKudos(Authentication auth,
                                           @RequestBody AddKudosRequest addKudosRequest) {
        return sponsorService.addKudos(auth, addKudosRequest);
    }
}
