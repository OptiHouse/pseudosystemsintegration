package com.pseudoorganization.pseudosystemsintegration.services;

import com.pseudoorganization.pseudosystemsintegration.models.Crime;
import com.pseudoorganization.pseudosystemsintegration.models.Race;
import com.pseudoorganization.pseudosystemsintegration.models.State;
import com.pseudoorganization.pseudosystemsintegration.models.Statistics;
import com.pseudoorganization.pseudosystemsintegration.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SeedService {

    private final StateRepository stateRepository;

    public void seed() {
        log.trace("Seeding database started");
        Crime larceny = Crime.builder()
                .name("larceny")
                .type("property")
                .rate("12")
                .total("123")
                .build();

        Crime murder = Crime.builder()
                .name("murder")
                .type("violent")
                .rate("12")
                .total("123")
                .build();

        Race white = Race.of("white", "123");
        Race asian = Race.of("asian", "12");


        Statistics statistics = Statistics.builder()
                .id(UUID.randomUUID())
                .date(java.time.LocalDate.ofYearDay(2020, 1))
                .crimes(List.of(larceny, murder))
                .population(List.of(white, asian))
                .build();

        State alabama = State.of("Alabama");
        alabama.setStatistics(List.of(statistics));

        stateRepository.save(alabama);

        log.trace(alabama.toString());
        log.trace("Seeding database finished");
    }
}
