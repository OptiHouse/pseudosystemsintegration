package com.pseudoorganization.pseudosystemsintegration.controllers;

import com.pseudoorganization.pseudosystemsintegration.services.normalization.csv.CSVImport;
import com.pseudoorganization.pseudosystemsintegration.services.normalization.json.JsonImport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Controller
public class EverythingController implements WebMvcConfigurer {

    JsonImport jsonImport = new JsonImport();

    CSVImport csvImport = new CSVImport();

    @PostMapping("/loadPopulationData")
    public ResponseEntity<?> loadPopulationData(){

        jsonImport.loadJSON();
        jsonImport.normalizeData();

        return ResponseEntity.ok(jsonImport.getJsonArray());
    }

    @PostMapping("/loadCrimeData")
    public ResponseEntity<?> loadCrimeData(){

        csvImport.loadCSV();

        return ResponseEntity.ok(csvImport.getRecords());
    }
}
