package com.teamolha.talantino.controller;

import ch.qos.logback.core.model.Model;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TalentController {

    // TODO:implement endpoint
    @GetMapping("/talents")
    public void talentsPage(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // User is authenticated
        } else {
            // User is not authenticated
        }
    }


}
