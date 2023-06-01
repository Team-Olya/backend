package com.teamolha.talantino.proof.model.entity;

import com.teamolha.talantino.general.validation.ProofStatus;
import com.teamolha.talantino.skill.model.entity.Skill;
import com.teamolha.talantino.talent.model.entity.Talent;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Proof {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "publication_date")
    @NotNull
    private LocalDateTime date;

    @NotBlank
    private String title;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JoinColumn(name = "talent_id")
    @NotNull
    private Talent talent;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH},
            mappedBy = "proofId")
    private List<Kudos> kudos;

    @NotBlank
    @ProofStatus
    private String status;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name = "proof_skills",
            joinColumns = @JoinColumn(name = "proof_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;
}
