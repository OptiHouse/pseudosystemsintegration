package com.pseudoorganization.pseudosystemsintegration.controllers.soap;

import com.pseudoorganization.pseudosystemsintegration.models.State;
import com.pseudoorganization.pseudosystemsintegration.services.StateService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.*;

@RequiredArgsConstructor
@Endpoint
public class SummaryEndpoint {
    private final StateService stateService;

    @Transactional(readOnly = true)
    @PayloadRoot(namespace = "http://test/summary", localPart = "getSummaryRequest")
    @ResponsePayload
    public GetSummaryResponse getSummary(@RequestPayload GetSummaryRequest request) {

        var states = getOptional(request.getStates());
        var years = getOptional(request.getYears());
        var crimes = getOptional(request.getCrimes());
        var races = getOptional(request.getRaces());

        List<State> filtered = stateService.getFiltered(states, years, crimes, races);

        Map<String, Double> summary = calculateSummary(filtered);

        return getSummaryResponse(summary);
    }

    private Optional<List<String>> getOptional(final List<String> list) {
        list.removeAll(Arrays.asList("", null));
        return list.isEmpty() ? Optional.empty() : Optional.of(list);
    }

    private Map<String, Double> calculateSummary(final List<State> filtered) {
        List<Double> maxPopulation = new ArrayList<>();
        List<Integer> totalCrimes = new ArrayList<>();
        List<Integer> totalCrimesProperty = new ArrayList<>();
        List<Integer> totalCrimesViolent = new ArrayList<>();

        filtered.forEach(state -> {
            state.getStatistics().forEach(statistic -> {
                double sumPopulation = statistic.getPopulation().stream().mapToDouble(population -> Double.parseDouble(population.getPopulation())).sum();
                maxPopulation.add(sumPopulation);
                totalCrimes.add(statistic.getCrimes().stream()
                        .mapToInt(crime -> Integer.parseInt(crime.getTotal()))
                        .sum());
                totalCrimesProperty.add(statistic.getCrimes().stream()
                        .filter(crime -> "Property".equals(crime.getType()))
                        .mapToInt(crime -> Integer.parseInt(crime.getTotal()))
                        .sum());
                totalCrimesViolent.add(statistic.getCrimes().stream()
                        .filter(crime -> "Violent".equals(crime.getType()))
                        .mapToInt(crime -> Integer.parseInt(crime.getTotal()))
                        .sum());
            });
        });

        double maxPercent = maxPopulation.stream().mapToInt(Double::intValue).max().orElse(0);
        double total = totalCrimes.stream().mapToInt(Integer::intValue).sum();
        double totalProperty = totalCrimesProperty.stream().mapToInt(Integer::intValue).sum();
        double totalViolent = totalCrimesViolent.stream().mapToInt(Integer::intValue).sum();

        Map<String, Double> map = new HashMap<>();
        map.put("maxPercent", maxPercent);
        map.put("total", total);
        map.put("totalProperty", totalProperty);
        map.put("totalViolent", totalViolent);

        return map;
    }

    private GetSummaryResponse getSummaryResponse(final Map<String, Double> results) {
        GetSummaryResponse getSummaryResponse = new GetSummaryResponse();

        Summary summary = new Summary();
        summary.setPopulationMaxPercent(String.valueOf(results.get("maxPercent")));
        summary.setCrimesTotal(String.valueOf(results.get("total").intValue()));
        summary.setCrimesTotalProperty(String.valueOf(results.get("totalProperty").intValue()));
        summary.setCrimesTotalViolent(String.valueOf(results.get("totalViolent").intValue()));

        getSummaryResponse.setSummary(summary);

        return getSummaryResponse;
    }
}
