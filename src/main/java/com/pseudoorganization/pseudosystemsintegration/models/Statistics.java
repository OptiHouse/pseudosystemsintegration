package com.pseudoorganization.pseudosystemsintegration.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDate date;

    @OneToMany
    @JoinColumn(name = "statistics_id", referencedColumnName = "id")
    private List<Race> population = List.of();

    @OneToMany
    @JoinColumn(name = "state_id", referencedColumnName = "id")
    private List<Crime> crimes = List.of();
}
