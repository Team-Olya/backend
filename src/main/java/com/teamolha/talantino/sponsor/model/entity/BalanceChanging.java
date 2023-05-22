package com.teamolha.talantino.sponsor.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceChanging {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "sponsor_id")
    Long sponsorId;

    int amount;

    Long proofId;

    Long skillId;

    LocalDateTime date;
}
