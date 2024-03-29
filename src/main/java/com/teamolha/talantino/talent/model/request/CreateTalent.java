package com.teamolha.talantino.talent.model.request;

import com.teamolha.talantino.general.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CreateTalent(
        @Email
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
