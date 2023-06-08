package com.pseudoorganization.pseudosystemsintegration.controllers.graph;

import com.pseudoorganization.pseudosystemsintegration.models.State;
import com.pseudoorganization.pseudosystemsintegration.services.StateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/graphs")
public class GraphController {

    private final StateService stateService;

    @GetMapping("")
    public List<State> graph() {
        return stateService.getAll();
    }
}
