package com.teamolha.talantino.talent.controller;

import com.teamolha.talantino.talent.model.request.CreateTalent;
import com.teamolha.talantino.talent.model.response.LoginResponse;
import com.teamolha.talantino.talent.service.TalentAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@AllArgsConstructor
public class TalentAuthController {
    TalentAuthService talentAuthService;

    @PostMapping("/talents/login")
    LoginResponse login(Authentication authentication) {
        return talentAuthService.login(authentication);
    }

    @PostMapping("/talents/register")
    @ResponseStatus(HttpStatus.CREATED)
    void register (@RequestBody @Valid CreateTalent talent, HttpServletRequest request) {
        talentAuthService.register(
                talent.email(),
                talent.password(),
                talent.name(),
                talent.surname(),
                talent.kind(),
                request
        );
    }

    @PostMapping("/email-confirm")
    LoginResponse emailConfirm(@RequestParam String token) {
        return talentAuthService.login(token);
    }
}
