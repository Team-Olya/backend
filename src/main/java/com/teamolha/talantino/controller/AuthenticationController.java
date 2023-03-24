package com.teamolha.talantino.controller;

import com.teamolha.talantino.model.request.CreateTalent;
import com.teamolha.talantino.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Validated
@AllArgsConstructor
public class AuthenticationController {
    AuthenticationService authService;

    @PostMapping("/talents/login")
    @ResponseStatus(HttpStatus.OK)
    String login(Authentication authentication) {
        return authService.generateJwtToken(authentication);
    }

    @PostMapping("/talents/register")
    @ResponseStatus(HttpStatus.CREATED)
    void register (@RequestBody @Valid CreateTalent talent) {
        authService.register(
                talent.email(),
                talent.password(),
                talent.name(),
                talent.surname(),
                talent.kind()
        );
    }
}
