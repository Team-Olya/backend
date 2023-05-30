package com.teamolha.talantino.notification.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class KudosNotificationDTO {

    private long id;

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
    private Date receivedDate;

    @NotNull
    @JsonIgnore
    private String toTalent;

    @NotNull
    private boolean isRead;
}
