package com.teamolha.talantino.controller;

import com.teamolha.talantino.model.response.TalentProfileResponse;
import com.teamolha.talantino.model.response.TalentsPageResponse;
import com.teamolha.talantino.service.TalentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@AllArgsConstructor
public class TalentController {

    TalentService talentService;

    // TODO:implement endpoint
    @GetMapping("/test")
    public void talentsPage(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // User is authenticated
        } else {
            // User is not authenticated
        }
    }

    @GetMapping("/talents")
    @ResponseStatus(HttpStatus.OK)
    public TalentsPageResponse listTalents(
            @RequestParam(required = false ,defaultValue = "9") Integer amount,
            @RequestParam(required = false, defaultValue = "0") Integer page
    ){
        return talentService.pageTalents(page,amount);
    }

    @GetMapping("/talents/{talent-id}")
    public TalentProfileResponse talentProfile(@PathVariable("talent-id") long talentId, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            return talentService.talentProfile(talentId);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }
}
