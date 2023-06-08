package com.pseudoorganization.pseudosystemsintegration.services.normalization.json;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;


import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Slf4j
@Getter
@Component
public class JsonImport {
    private JSONArray jsonArray;

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

    public void normalizeData() {
        log.info("Start normalizing JSON data.");

        for (Object jsonObject : jsonArray) {
            if (jsonObject instanceof JSONObject) {
                JSONObject node = (JSONObject) jsonObject;

                HashMap<String, String> hashMap = new HashMap<String, String>();

                hashMap.put("nhwhite_est", node.get("nhwhite_est").toString());
                hashMap.put("black_est", node.get("black_est").toString());
                hashMap.put("asian_est", node.get("asian_est").toString());
                hashMap.put("hisp_est", node.get("hisp_est").toString());
                hashMap.put("nhopi_est", node.get("nhopi_est").toString());
                hashMap.put("other_est", node.get("other_est").toString());

                hashMap.entrySet().stream().forEach(e -> {
                    log.info(e.getKey() + " " + e.getValue());
                });

            }
        }
    }
}

