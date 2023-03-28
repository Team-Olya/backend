package com.teamolha.talantino.controller;

import com.teamolha.talantino.model.request.TalentUpdateRequest;
import com.teamolha.talantino.model.response.TalentProfileResponse;
import com.teamolha.talantino.model.response.TalentsPageResponse;
import com.teamolha.talantino.model.response.UpdatedTalentResponse;
import com.teamolha.talantino.service.TalentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
public class TalentController {
    TalentService talentService;

    @GetMapping("/talents")
    public TalentsPageResponse listTalents (
            @RequestParam(required = false ,defaultValue = "9") Integer amount,
            @RequestParam(required = false, defaultValue = "0") Integer page
    ) {
        return talentService.pageTalents(page,amount);
    }

    @GetMapping("/talents/{talent-id}")
    public TalentProfileResponse talentProfile(@PathVariable("talent-id") long talentId) {
        return talentService.talentProfile(talentId);
    }

    @PatchMapping("/talents/{talent-id}")
    public UpdatedTalentResponse updateTalentProfile(@PathVariable("talent-id") long talentId,
                                                     @RequestBody TalentUpdateRequest talentUpdateRequest,
                                                     Authentication auth) {
        return talentService.updateTalentProfile(talentId, auth.getName(), talentUpdateRequest);
    }

    @DeleteMapping("/talents/{talent-id}")
    public void deleteTalent(@PathVariable("talent-id") long talentId, Authentication auth) {
        talentService.deleteTalent(talentId, auth.getName());
    }
}
