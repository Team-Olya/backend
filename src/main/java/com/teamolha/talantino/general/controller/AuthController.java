package com.teamolha.talantino.general.controller;

import com.teamolha.talantino.general.service.AuthService;
import com.teamolha.talantino.sponsor.model.request.CreateSponsor;
import com.teamolha.talantino.sponsor.service.SponsorService;
import com.teamolha.talantino.talent.model.request.CreateTalent;
import com.teamolha.talantino.talent.model.response.LoginResponse;
import com.teamolha.talantino.talent.service.TalentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@Slf4j
@AllArgsConstructor
public class AuthController {
    TalentService talentService;
    AuthService authService;
    SponsorService sponsorService;

    @GetMapping("/auth/me")
    Object getMyProfile(Authentication authentication) {
        return authService.myProfile(authentication);
    }

    @PostMapping("/login")
    LoginResponse login(Authentication authentication) {
        return authService.login(authentication);
    }

    @PostMapping("/talents/register")
    @ResponseStatus(HttpStatus.CREATED)
    void talentRegister (@RequestBody @Valid CreateTalent talent, HttpServletRequest request) {
        talentService.register(
                talent.email(),
                talent.password(),
                talent.name(),
                talent.surname(),
                talent.kind(),
                request
        );
    }

    @PostMapping("/sponsor/register")
    @ResponseStatus(HttpStatus.CREATED)
    void sponsorRegister (@RequestBody @Valid CreateSponsor sponsor, HttpServletRequest request) {
        sponsorService.register (
                sponsor.email(),
                sponsor.password(),
                sponsor.name(),
                sponsor.surname(),
                request
        );
    }

    @PostMapping("/email-confirm")
    LoginResponse emailConfirm(@RequestParam String token) {
        return authService.login(token);
    }
}
