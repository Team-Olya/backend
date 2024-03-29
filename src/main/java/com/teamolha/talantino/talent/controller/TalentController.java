package com.teamolha.talantino.talent.controller;

import com.teamolha.talantino.talent.model.request.TalentUpdateRequest;
import com.teamolha.talantino.talent.model.response.*;
import com.teamolha.talantino.talent.service.TalentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
public class TalentController {
    TalentService talentService;

    @GetMapping("/talents")
    public TalentsPageResponse listTalents (
            @RequestParam(required = false ,defaultValue = "9") Integer amount,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false) String skills
    ) {
        return talentService.pageTalents(page, amount, skills);
    }

    @GetMapping("/talents/{talent-id}")
    public TalentFullResponse talentProfile(@PathVariable("talent-id") long talentId) {
        return talentService.talentProfile(talentId);
    }

    @PatchMapping("/talents/{talent-id}")
    public UpdatedTalentResponse updateTalentProfile(@PathVariable("talent-id") long talentId,
                                                     @RequestBody @Valid TalentUpdateRequest talentUpdateRequest,
                                                     Authentication auth) {
        return talentService.updateTalentProfile(talentId, auth.getName(), talentUpdateRequest);
    }

    @DeleteMapping("/talents/{talent-id}")
    public void deleteTalent(@PathVariable("talent-id") long talentId, Authentication auth, HttpServletRequest request) {
        talentService.deleteTalent(request, talentId, auth.getName());
    }

    @GetMapping("/talents/kinds")
    public KindsDTO getTalentKinds(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return talentService.getTalentKinds(page, size);
    }

    @GetMapping("/talents/{talent-id}/statistic")
    public TalentStatistic getTalentStatistic(@PathVariable("talent-id") long talentId, Authentication auth) {
        return talentService.getStatistic(talentId, auth.getName());
    }

    @PostMapping("/talents/{talent-id}/report")
    public ReportedTalentDTO reportTalent(Authentication auth, @PathVariable("talent-id") Long talentId,
                                          HttpServletRequest request) {
        return talentService.reportTalent(auth, talentId, request);
    }
}
