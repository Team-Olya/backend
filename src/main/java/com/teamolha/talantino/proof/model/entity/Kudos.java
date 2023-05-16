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
    Long id;

    @Column(name = "sponsor_id")
    Long sponsorId;

    @Column(name = "proof_id")
    Long proofId;

    @Column(name = "skill_id")
    Long skillId;

    int amount;
}
