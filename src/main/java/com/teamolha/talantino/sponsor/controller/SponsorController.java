package com.teamolha.talantino.sponsor.controller;

import com.teamolha.talantino.sponsor.model.request.AddKudosRequest;
import com.teamolha.talantino.sponsor.model.request.SponsorUpdateRequest;
import com.teamolha.talantino.sponsor.model.response.SponsorProfileResponse;
import com.teamolha.talantino.sponsor.model.response.UpdatedSponsorResponse;
import com.teamolha.talantino.sponsor.service.SponsorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class SponsorController {

    private SponsorService sponsorService;

    @PostMapping("/sponsors/balance")
    public SponsorProfileResponse addKudos(Authentication auth,
                                           @RequestBody AddKudosRequest addKudosRequest) {
        return sponsorService.addKudos(auth, addKudosRequest);
    }
    @PatchMapping("/sponsors/{sponsor-id}")
    public UpdatedSponsorResponse updateSponsorProfile(@PathVariable("sponsor-id") long sponsorId,
                                                       @RequestBody SponsorUpdateRequest sponsorUpdateRequest,
                                                       Authentication auth) {
        return sponsorService.updateSponsorProfile(sponsorId, auth.getName(), sponsorUpdateRequest);
    }

    @DeleteMapping("/sponsors/{sponsor-id}")
    public void deleteSponsor(HttpServletRequest request, Authentication auth,
                              @PathVariable("sponsor-id") long sponsorId) {
        sponsorService.deleteSponsor(request, auth, sponsorId);
    }

    @PostMapping("/sponsors/recover")
    public void recoverSponsor(@RequestParam String token) {
        sponsorService.recoverSponsor(token);
    }
}
