package com.teamolha.talantino.controller;

import com.teamolha.talantino.model.response.TalentsPageResponse;
import com.teamolha.talantino.service.TalentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


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

}
