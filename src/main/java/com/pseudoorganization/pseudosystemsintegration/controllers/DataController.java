package com.pseudoorganization.pseudosystemsintegration.controllers;

import com.pseudoorganization.pseudosystemsintegration.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/data")
public class DataController {

    private final StateRepository stateRepository;

    @GetMapping("")
    public String data() {
        return "data";
    }
}
