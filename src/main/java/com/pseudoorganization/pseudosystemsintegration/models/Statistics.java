package com.pseudoorganization.pseudosystemsintegration.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String year;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "statistics_id", referencedColumnName = "id")
    private List<Race> population = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "state_id", referencedColumnName = "id")
    private List<Crime> crimes = new ArrayList<>();

    public static Statistics of(final String year) {
        return new Statistics(UUID.randomUUID(), year, new ArrayList<>(), new ArrayList<>());
    }
}
