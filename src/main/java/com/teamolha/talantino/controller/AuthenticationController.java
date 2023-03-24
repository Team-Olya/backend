package com.teamolha.talantino.controller;

import com.teamolha.talantino.service.AuthenticationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthenticationController {

    AuthenticationService authService;

    AuthenticationController(AuthenticationService authService){
        this.authService = authService;
    }

    @PostMapping("/talents/login")
    String login(Authentication authentication) {
        return authService.generateJwtToken(authentication);
    }

}
