package com.pseudoorganization.pseudosystemsintegration.services;

import com.pseudoorganization.pseudosystemsintegration.models.State;
import com.pseudoorganization.pseudosystemsintegration.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StateService {

    private final StateRepository stateRepository;

    public List<State> getAll() {
        return stateRepository.findAll();
    }

    public List<State> getFiltered(Optional<List<String>> states,
                                   Optional<List<String>> years,
                                   Optional<List<String>> crimes,
                                   Optional<List<String>> races) {

        List<State> all = getAll();

        states.ifPresent(strings -> all.removeIf(
                state -> !strings.contains(state.getName())));

        years.ifPresent(integers -> all.forEach(
                state -> state.getStatistics().removeIf(
                        statistic -> !integers.contains(statistic.getYear()))));

        crimes.ifPresent(strings -> all.forEach(
                state -> state.getStatistics().forEach(
                        statistic -> statistic.getCrimes().removeIf(crime -> !strings.contains(crime.getName())))));

        races.ifPresent(strings -> all.forEach(
                state -> state.getStatistics().forEach(
                        statistic -> statistic.getPopulation().removeIf(population -> !strings.contains(population.getName())))));

        return all;
    }

    public State findByName(String name) {
        return stateRepository.findByName(name).orElse(null);
    }

    public void save(State state) {
        stateRepository.save(state);
    }
}
