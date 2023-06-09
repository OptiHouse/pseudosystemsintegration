package com.pseudoorganization.pseudosystemsintegration.controllers.data;

import com.pseudoorganization.pseudosystemsintegration.services.SeedService;
import com.pseudoorganization.pseudosystemsintegration.services.normalization.csv.CSVImport;
import com.pseudoorganization.pseudosystemsintegration.services.normalization.json.JsonImport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/data")
public class DataController {

    private final SeedService seedService;

    private final JsonImport jsonImport;

    private final CSVImport csvImport;

    @PostMapping("/loadPopulationData")
    public ResponseEntity<?> loadPopulationData() {

        jsonImport.loadJSON();
        jsonImport.normalizeData();

        return ResponseEntity.ok(jsonImport.getJsonArray());
    }

    @PostMapping("/loadCrimeData")
    public ResponseEntity<?> loadCrimeData() {

        csvImport.loadCSV();
        csvImport.normalizeData();

        return ResponseEntity.ok(csvImport.getRecords());
    }

    @GetMapping("/seed")
    public String seed() {
        seedService.seed();
        return "seeded";
    }
}
