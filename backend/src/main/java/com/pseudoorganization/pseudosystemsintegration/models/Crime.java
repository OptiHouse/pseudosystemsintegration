package com.pseudoorganization.pseudosystemsintegration.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Crime {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String type;
    private String rate;
    private String total;

    public static Crime of(final String name, final String type, final String rate, final String total) {
        return new Crime(UUID.randomUUID(), name, type, rate, total);
    }
}
