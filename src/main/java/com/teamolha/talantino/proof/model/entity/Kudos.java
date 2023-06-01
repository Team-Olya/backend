package com.teamolha.talantino.proof.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Kudos {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sponsor_id")
    private Long sponsorId;

    @Column(name = "proof_id")
    private Long proofId;

    @Column(name = "skill_id")
    private Long skillId;

    private int amount;
}
