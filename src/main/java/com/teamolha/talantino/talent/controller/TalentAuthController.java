package com.teamolha.talantino.talent.controller;

import com.teamolha.talantino.talent.model.request.CreateTalent;
import com.teamolha.talantino.talent.model.response.LoginResponse;
import com.teamolha.talantino.talent.service.TalentAuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    void register (@RequestBody @Valid CreateTalent talent) {
        talentAuthService.register(
                talent.email(),
                talent.password(),
                talent.name(),
                talent.surname(),
                talent.kind()
        );
    }
}
