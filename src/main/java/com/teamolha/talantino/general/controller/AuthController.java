package com.teamolha.talantino.general.controller;

import com.teamolha.talantino.talent.model.response.TalentProfileResponse;
import com.teamolha.talantino.talent.service.TalentService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

    TalentService talentService;

    @GetMapping("/auth/me")
    TalentProfileResponse getMyProfile(Authentication authentication) {
        return talentService.talentProfile(authentication.getName());
    }
}
