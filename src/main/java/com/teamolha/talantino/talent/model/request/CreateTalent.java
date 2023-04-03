package com.teamolha.talantino.talent.model.request;

import com.teamolha.talantino.validation.Password;
import jakarta.validation.constraints.NotNull;

public record CreateTalent(
        @NotNull
        String email,
        @NotNull
        @Password
        String password,
        @NotNull
        String name,
        @NotNull
        String surname,
        @NotNull
        String kind
) {
}
