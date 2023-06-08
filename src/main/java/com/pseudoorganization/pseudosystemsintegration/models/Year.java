package com.pseudoorganization.pseudosystemsintegration.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Year {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDate date;

    @OneToMany
    @JoinColumn(name = "yearId", referencedColumnName = "id")
    private List<Crime> crimes;

    @OneToMany
    @JoinColumn(name = "yearId", referencedColumnName = "id")
    private List<Race> population;
}
