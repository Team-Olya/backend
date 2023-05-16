package com.teamolha.talantino.account.model.entity;

import com.teamolha.talantino.account.model.AccountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@Entity
@Inheritance(strategy =  InheritanceType.JOINED)
@Getter
@Setter
@ToString
@NoArgsConstructor
@SuperBuilder
public abstract class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @ElementCollection(fetch = FetchType.EAGER)
    private Collection<String> authorities;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    abstract public String getAvatar();

}
