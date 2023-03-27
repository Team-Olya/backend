package com.teamolha.talantino.model.request;

import com.teamolha.talantino.model.entity.Kind;
import com.teamolha.talantino.model.entity.Link;
import com.teamolha.talantino.model.response.LinkResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TalentUpdateRequest(
        @NotNull
        String name,
        @NotNull
        String surname,
        @NotNull
        String kind,
        @NotNull
        String avatar,
        @NotNull
        String description,
        @NotNull
        int experience,
        @NotNull
        String location,
        @NotNull
        List<String> links
) {
}
