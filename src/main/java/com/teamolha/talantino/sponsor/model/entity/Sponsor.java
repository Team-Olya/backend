package com.teamolha.talantino.sponsor.model.entity;

import com.teamolha.talantino.account.model.entity.Account;
import com.teamolha.talantino.proof.model.entity.Kudos;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
public class Sponsor extends Account {
    @NotNull
    private Long balance;

    @Column(columnDefinition = "TEXT")
    private String avatar;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH},
            mappedBy = "sponsorId")
    @ToString.Exclude
    private List<Kudos> kudos;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH},
            mappedBy = "sponsorId")
    @ToString.Exclude
    private List<BalanceChanging> balanceChangings;
}
