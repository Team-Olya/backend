package com.teamolha.talantino.admin.model.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateAdmin(
        @NotNull
        String email,
        @NotNull
        String password,
        @NotNull
        String name,
        @NotNull
        String surname
) {}
