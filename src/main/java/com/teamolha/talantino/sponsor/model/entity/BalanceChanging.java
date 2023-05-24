package com.teamolha.talantino.sponsor.model.entity;

import com.teamolha.talantino.talent.model.entity.Talent;
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
    private Long id;

    @Column(name = "sponsor_id")
    private Long sponsorId;

    private int amount;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "talent_id", nullable = true)
    Talent talent;

    private LocalDateTime date;
}
