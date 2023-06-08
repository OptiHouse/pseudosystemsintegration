package com.pseudoorganization.pseudosystemsintegration.controllers.data;

import com.pseudoorganization.pseudosystemsintegration.services.SeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/data")
public class DataController {

    private final SeedService seedService;

    @GetMapping("")
    public String data() {
        return "data";
    }

    @GetMapping("/seed")
    public String seed() {
        seedService.seed();
        return "seeded";
    }
}
