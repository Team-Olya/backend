package com.teamolha.talantino.talent.model.entity;

import com.teamolha.talantino.account.model.entity.Account;
import com.teamolha.talantino.admin.model.AccountStatus;
import com.teamolha.talantino.proof.model.entity.Proof;
import com.teamolha.talantino.skill.model.entity.Skill;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
public class Talent extends Account {
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long talentId;

    @NotNull
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JoinColumn(name = "kind_id")
    private Kind kind;

    @Column(columnDefinition = "TEXT")
    private String avatar;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int experience;

    private String location;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH},
            mappedBy = "talent")
    @ToString.Exclude
    private List<Link> links;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH},
            mappedBy = "talent")
    @ToString.Exclude
    private List<Proof> proofs;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "talent_skills",
            joinColumns = @JoinColumn(name = "talent_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    @ToString.Exclude
    private List<Skill> skills;

    private Date verificationExpireDate;

    private String verificationToken;
}
