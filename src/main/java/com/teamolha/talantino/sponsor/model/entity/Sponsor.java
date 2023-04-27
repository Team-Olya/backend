package com.teamolha.talantino.sponsor.model.entity;

import com.teamolha.talantino.proof.model.entity.Kudos;
import com.teamolha.talantino.sponsor.model.SponsorStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sponsor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private Long balance;

    @Column(columnDefinition = "TEXT")
    private String avatar;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH},
            mappedBy = "sponsorId")
    private List<Kudos> kudos;

    @ElementCollection(fetch = FetchType.EAGER)
    private Collection<String> authorities;

    @Enumerated(EnumType.STRING)
    private SponsorStatus status;

    private Date deletionDate;

    private String deletionToken;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH},
            mappedBy = "sponsorId")
    private List<BalanceAdding> balanceAdding;
}
