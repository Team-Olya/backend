package com.teamolha.talantino.talent.model.entity;

import com.teamolha.talantino.admin.model.AccountStatus;
import com.teamolha.talantino.proof.model.entity.Proof;
import com.teamolha.talantino.skill.model.entity.Skill;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
//@Table(name = "talent")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Talent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private String email;

    @NotNull
    private String password;

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

    @ElementCollection(fetch = FetchType.EAGER)
    private Collection<String> authorities;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    private Date verificationExpireDate;

    private String verificationToken;
}
