package com.pseudoorganization.pseudosystemsintegration.services;

import com.pseudoorganization.pseudosystemsintegration.models.State;
import com.pseudoorganization.pseudosystemsintegration.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StateService {

    private final StateRepository stateRepository;

    public List<State> getAll() {
        return stateRepository.findAll();
    }
}
