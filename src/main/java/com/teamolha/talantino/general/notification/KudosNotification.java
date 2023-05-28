package com.teamolha.talantino.general.notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class KudosNotification {

    @NotNull
    private String fromSponsor;

    private String sponsorAvatar;

    @NotNull
    private int amount;

    @NotNull
    private String proofTitle;

    @NotNull
    private long proofId;

    @NotNull
    @JsonIgnore
    private String toTalent;
}
