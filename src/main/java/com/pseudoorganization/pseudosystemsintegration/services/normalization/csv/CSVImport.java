package com.pseudoorganization.pseudosystemsintegration.services.normalization.csv;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Getter
public class CSVImport {

    private static final String COMMA_DELIMITER = ",";

    List<List<String>> records = new ArrayList<>();

    public void loadCSV(){
        InputStream inputStream = CSVImport.class.getResourceAsStream("/static/state_crime.csv");
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        try (BufferedReader br = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                records.add(Arrays.asList(values));
            }
            log.info("CSV file loaded successfully.");
            log.trace(records.toString());
        }catch (Exception e){
            log.error("Error while loading CSV file: " + e.getMessage());
        }
    }
}
