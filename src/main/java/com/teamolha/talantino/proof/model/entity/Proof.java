package com.teamolha.talantino.proof.model.entity;

import com.teamolha.talantino.proof.model.Status;
import com.teamolha.talantino.talent.model.entity.Talent;
import com.teamolha.talantino.validation.ProofStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

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
    @NotBlank
    private Date date;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JoinColumn(name = "talent_id")
    @NotBlank
    private Talent talent;

    @NotBlank
    private String status;
}