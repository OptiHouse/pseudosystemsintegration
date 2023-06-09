package com.pseudoorganization.pseudosystemsintegration.services.normalization.csv;

import com.pseudoorganization.pseudosystemsintegration.models.Crime;
import com.pseudoorganization.pseudosystemsintegration.models.State;
import com.pseudoorganization.pseudosystemsintegration.models.Statistics;
import com.pseudoorganization.pseudosystemsintegration.services.StateService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Getter
@Service
public class CSVImport {

    private static final String COMMA_DELIMITER = ",";

    List<List<String>> records = new ArrayList<>();

    StateService stateService;

    public CSVImport(StateService stateService) {
        this.stateService = stateService;
    }

    public void loadCSV() {
        InputStream inputStream = CSVImport.class.getResourceAsStream("/static/state_crime.csv");
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        try (BufferedReader br = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                List<String> arrayList = Arrays.asList(values);
                arrayList.replaceAll(e -> e.replace("\"", ""));
                records.add(arrayList);
            }
            log.info("CSV file loaded successfully.");
            log.trace(records.toString());
        } catch (Exception e) {
            log.error("Error while loading CSV file: " + e.getMessage());
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void normalizeData() {
        log.info("Start normalizing CSV data.");

        records.remove(0);

        records.stream().forEach(line -> {
            String stateName = line.get(0);
            State state = stateService.findByName(stateName);
            if (state == null) {
                state = State.of(stateName);

            }
            List<Statistics> statisticsList = state.getStatistics();

            String year = line.get(1);

            Statistics statistics = statisticsList.stream()
                    .filter(s -> s.getYear().equals(year))
                    .findFirst().orElse(Statistics.of(year));

            List<Crime> crimes = statistics.getCrimes();

            String burglaryRate = line.get(4);
            String larcenyRate = line.get(5);
            String motorRate = line.get(6);

            String assaultRate = line.get(8);
            String murderRate = line.get(9);
            String rapeRate = line.get(10);
            String robberyRate = line.get(11);

            String burglaryTotal = line.get(13);
            String larcenyTotal = line.get(14);
            String motorTotal = line.get(15);

            String assaultTotal = line.get(17);
            String murderTotal = line.get(18);
            String rapeTotal = line.get(19);
            String robberyTotal = line.get(20);

            HashMap<String, List<String>> crimesMap = new HashMap<>();

            crimesMap.put("Burglary", new ArrayList<>(Arrays.asList(burglaryRate, burglaryTotal)));
            crimesMap.put("Larceny", new ArrayList<>(Arrays.asList(larcenyRate, larcenyTotal)));
            crimesMap.put("Motor", new ArrayList<>(Arrays.asList(motorRate, motorTotal)));

            crimesMap.put("Assault", new ArrayList<>(Arrays.asList(assaultRate, assaultTotal)));
            crimesMap.put("Murder", new ArrayList<>(Arrays.asList(murderRate, murderTotal)));
            crimesMap.put("Rape", new ArrayList<>(Arrays.asList(rapeRate, rapeTotal)));
            crimesMap.put("Robbery", new ArrayList<>(Arrays.asList(robberyRate, robberyTotal)));


            crimesMap.entrySet().stream()
                    .filter(entry -> crimes.stream().noneMatch(crime -> crime.getName().equals(entry.getKey())))
                    .forEach(entry -> crimes.add(Crime.of(entry.getKey(), getCrimeType(entry.getKey()), entry.getValue().get(0), entry.getValue().get(1))));

            statistics.setCrimes(crimes);

            state.getStatistics().add(statistics);

            stateService.save(state);
        });

    }

    private String getCrimeType(String crimeName) {
        switch (crimeName) {
            case "Burglary", "Larceny", "Motor" -> {
                return "Property";
            }
            case "Assault", "Murder", "Rape", "Robbery" -> {
                return "Violent";
            }
            default -> {
                return "";
            }
        }
    }
}
