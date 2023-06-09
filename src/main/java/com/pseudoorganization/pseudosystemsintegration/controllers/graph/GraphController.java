package com.pseudoorganization.pseudosystemsintegration.controllers.graph;

import com.pseudoorganization.pseudosystemsintegration.models.State;
import com.pseudoorganization.pseudosystemsintegration.services.StateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/graphs")
public class GraphController {

    private final StateService stateService;

    @GetMapping("")
    public List<State> graph(@RequestParam Optional<List<String>> states,
                             @RequestParam Optional<List<Integer>> years,
                             @RequestParam Optional<List<String>> crimes,
                             @RequestParam Optional<List<String>> races) {

        return stateService.getFiltered(states, years, crimes, races);
    }
}
