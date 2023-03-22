package com.teamolha.talantino.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;

@Entity
//@Table(name = "talent")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Talent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String surname;

    private String email;

    private String password;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JoinColumn(name = "kind_id")
    private Kind kind;

    private String avatar;

    private String description;

    private int experience;

    private String location;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH},
            mappedBy = "talent")
    private List<Link> links;

    @ElementCollection(fetch = FetchType.EAGER)
    private Collection<String> authorities;
}
