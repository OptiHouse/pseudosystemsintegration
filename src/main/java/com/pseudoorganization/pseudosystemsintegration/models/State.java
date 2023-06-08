package com.pseudoorganization.pseudosystemsintegration.models;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany
    @JoinColumn(name = "state_id", referencedColumnName = "id")
    private List<Statistics> statistics = List.of();

    public static State of(String name) {
        return new State(UUID.randomUUID(), name, List.of());
    }
}
