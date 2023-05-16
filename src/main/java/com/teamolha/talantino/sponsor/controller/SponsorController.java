package com.teamolha.talantino.sponsor.controller;

import com.teamolha.talantino.sponsor.model.request.AddKudosRequest;
import com.teamolha.talantino.sponsor.model.request.SponsorUpdateRequest;
import com.teamolha.talantino.sponsor.model.response.BalanceAddingDTO;
import com.teamolha.talantino.sponsor.model.response.SponsorKudos;
import com.teamolha.talantino.sponsor.model.response.SponsorProfileResponse;
import com.teamolha.talantino.sponsor.model.response.UpdatedSponsorResponse;
import com.teamolha.talantino.sponsor.service.SponsorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class SponsorController {

    private SponsorService sponsorService;

    @PostMapping("/sponsors/balance")
    public SponsorProfileResponse addKudos(Authentication auth,
                                           @RequestBody AddKudosRequest addKudosRequest) {
        return sponsorService.addKudos(auth, addKudosRequest);
    }

    @GetMapping("/sponsors/balance/history")
    public List<BalanceAddingDTO> getBalanceAddingHistory(Authentication auth) {
        return sponsorService.getBalanceAddingHistory(auth);
    }

//    @GetMapping("/sponsors/kudos")
//    public List<SponsorKudos> getKudosHistory(Authentication auth) {
//        return sponsorService.getKudosHistory(auth);
//    }

    @PatchMapping("/sponsors/{sponsor-id}")
    public UpdatedSponsorResponse updateSponsorProfile(@PathVariable("sponsor-id") long sponsorId,
                                                       @RequestBody @Valid SponsorUpdateRequest sponsorUpdateRequest,
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
