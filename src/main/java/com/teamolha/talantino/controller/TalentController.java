package com.teamolha.talantino.controller;

import com.teamolha.talantino.model.request.TalentUpdateRequest;
import com.teamolha.talantino.model.response.TalentProfileResponse;
import com.teamolha.talantino.model.response.TalentsPageResponse;
import com.teamolha.talantino.model.response.UpdatedTalentResponse;
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

    @GetMapping("/talents")
    @ResponseStatus(HttpStatus.OK)
    public TalentsPageResponse listTalents(
            @RequestParam(required = false ,defaultValue = "9") Integer amount,
            @RequestParam(required = false, defaultValue = "0") Integer page
    ){
        return talentService.pageTalents(page,amount);
    }

    @GetMapping("/talents/{talent-id}")
    @ResponseStatus(HttpStatus.OK)
    public TalentProfileResponse talentProfile(@PathVariable("talent-id") long talentId, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            return talentService.talentProfile(talentId);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    @PatchMapping("/talents/{talent-id}")
    @ResponseStatus(HttpStatus.OK)
    public UpdatedTalentResponse updateTalentProfile(@PathVariable("talent-id") long talentId,
                                                     @RequestBody TalentUpdateRequest talentUpdateRequest,
                                                     Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            return talentService.updateTalentProfile(talentId, auth.getName(), talentUpdateRequest);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

}
