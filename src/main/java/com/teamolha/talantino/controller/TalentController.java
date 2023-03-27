package com.teamolha.talantino.controller;

import com.teamolha.talantino.model.request.TalentUpdateRequest;
import com.teamolha.talantino.model.response.TalentProfileResponse;
import com.teamolha.talantino.model.response.TalentsPageResponse;
import com.teamolha.talantino.model.response.UpdatedTalentResponse;
import com.teamolha.talantino.service.TalentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
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

    @DeleteMapping("/talents/{talent-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTalent(@PathVariable("talent-id") long talentId, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            log.info("auth name = {}", auth.getName());
            talentService.deleteTalent(talentId, auth.getName());
        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
