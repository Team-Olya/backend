package com.teamolha.talantino.talent.model.request;

import java.util.List;

public record TalentUpdateRequest(
        String name,
        String surname,
        String kind,
        String avatar,
        String description,
        Integer experience,
        String location,
        List<String> links
) {
}
