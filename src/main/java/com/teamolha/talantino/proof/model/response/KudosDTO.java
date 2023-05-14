package com.teamolha.talantino.proof.model.response;

import com.teamolha.talantino.sponsor.model.response.ShortSponsorDTO;
import lombok.Builder;

@Builder
public record KudosDTO (
        ShortSponsorDTO sponsor,
        int amountOfKudos
) {
}
