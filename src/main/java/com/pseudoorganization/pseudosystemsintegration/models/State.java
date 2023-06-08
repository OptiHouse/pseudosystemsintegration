package com.pseudoorganization.pseudosystemsintegration.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "state_id", referencedColumnName = "id")
    private List<Statistics> statistics = new ArrayList<>();

    public static State of(final String name) {
        return new State(UUID.randomUUID(), name, new ArrayList<>());
    }
}
