package com.pseudoorganization.pseudosystemsintegration.services.normalization.json;

import com.pseudoorganization.pseudosystemsintegration.models.Race;
import com.pseudoorganization.pseudosystemsintegration.models.State;
import com.pseudoorganization.pseudosystemsintegration.models.Statistics;
import com.pseudoorganization.pseudosystemsintegration.services.StateService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Getter
@Service
public class JsonImport {
    private JSONArray jsonArray;
    private final StateService stateService;

    public JsonImport(StateService stateService) {
        this.stateService = stateService;
    }

    public void loadJSON() {
        try {
            JSONParser jsonParser = new JSONParser();
            InputStream inputStream = JsonImport.class.getResourceAsStream("/static/population.json");
            String jsonAsText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            jsonArray = (JSONArray) jsonParser.parse(jsonAsText);


            log.info("JSON file loaded successfully.");
            log.trace(jsonArray.toString());
        } catch (Exception e) {
            log.error("Error while loading JSON file: " + e.getMessage());
        }
    }

    //    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void normalizeData() {
        log.info("Start normalizing JSON data.");

        for (Object jsonObject : jsonArray) {
            if (jsonObject instanceof JSONObject node) {
                String year = node.get("year").toString().split("-")[0];
                String stateName = node.get("name").toString();

                State state = stateService.findByName(stateName);
                if (state == null) {
                    state = State.of(stateName);
                }
                List<Statistics> statisticsList = state.getStatistics();
                Statistics statistics = statisticsList.stream().filter(s -> s.getYear().equals(year)).findFirst().orElse(null);

                if (statistics != null) {
                    statisticsList.removeIf(s -> s.getYear().equals(year));
                } else {
                    statistics = Statistics.of(year);
                }
                List<Race> raceList = statistics.getPopulation();
                HashMap<String, String> raceMap = new HashMap<>();

                raceMap.put("nhwhite_est", node.get("nhwhite_est").toString());
                raceMap.put("black_est", node.get("black_est").toString());
                raceMap.put("asian_est", node.get("asian_est").toString());
                raceMap.put("hisp_est", node.get("hisp_est").toString());
                raceMap.put("nhopi_est", node.get("nhopi_est").toString());
                raceMap.put("other_est", node.get("other_est").toString());

                raceMap.entrySet().stream()
                        .filter(entry -> raceList.stream().noneMatch(race -> race.getName().equals(entry.getKey())))
                        .forEach(entry -> raceList.add(Race.of(entry.getKey(), entry.getValue())));

                statistics.setPopulation(raceList);
                statisticsList.add(statistics);
                state.setStatistics(statisticsList);
                stateService.save(state);
            }
        }
    }
}

