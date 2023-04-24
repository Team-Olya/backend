package com.teamolha.talantino.sponsor.controller;

import com.teamolha.talantino.sponsor.model.response.SponsorProfileResponse;
import com.teamolha.talantino.sponsor.service.SponsorService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SponsorController {

    private SponsorService sponsorService;

    @PostMapping("/sponsors/balance")
    public SponsorProfileResponse addFunds(Authentication auth,
                                           @RequestParam(value = "amount") Long amount) {
        return sponsorService.addFunds(auth, amount);
    }
}
