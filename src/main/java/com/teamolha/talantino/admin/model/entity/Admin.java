package com.teamolha.talantino.admin.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    private String login;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private Collection<String> authorities;
}
