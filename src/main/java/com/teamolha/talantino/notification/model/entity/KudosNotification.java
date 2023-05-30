package com.teamolha.talantino.notification.model.entity;

import com.teamolha.talantino.proof.model.entity.Proof;
import com.teamolha.talantino.sponsor.model.entity.Sponsor;
import com.teamolha.talantino.talent.model.entity.Talent;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KudosNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @NotNull
    private Sponsor fromSponsor;

    @NotNull
    private int amount;

    @OneToOne
    @NotNull
    private Proof proof;

    private Date receivingDate;

    private Date expirationDate;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JoinColumn(name = "to_talent_id")
    @NotNull
    private Talent toTalent;
}
