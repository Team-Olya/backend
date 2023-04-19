package com.teamolha.talantino.proof.model.entity;

import com.teamolha.talantino.proof.model.Status;
import com.teamolha.talantino.sponsor.model.entity.Sponsor;
import com.teamolha.talantino.talent.model.entity.Talent;
import com.teamolha.talantino.validation.ProofStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
    private String description; // TODO: ограничить в сервисе

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
    private String status;
}
