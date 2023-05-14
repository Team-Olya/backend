package com.teamolha.talantino.sponsor.model.request;

import com.teamolha.talantino.general.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateSponsor (
        @NotBlank
        String name,

        @NotBlank
        String surname,

        @Email
        String email,

        @Password
        String password
) {
}
