package com.teamolha.talantino.admin.model.response;

import lombok.Builder;

@Builder
public record AdminProfile(
        String role,

        long id,

        String name,

        String surname
) {
}
