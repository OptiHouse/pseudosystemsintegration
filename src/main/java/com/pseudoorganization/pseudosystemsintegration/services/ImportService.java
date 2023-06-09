package com.pseudoorganization.pseudosystemsintegration.services;

import com.pseudoorganization.pseudosystemsintegration.models.State;
import com.pseudoorganization.pseudosystemsintegration.models.Statistics;
import com.pseudoorganization.pseudosystemsintegration.services.parsers.ParserFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImportService {

    private final ParserFactory parserFactory;
    private final StateService stateService;

    @Transactional
    public void importFile(final InputStream file, final String fileName) {
        String extension = FilenameUtils.getExtension(fileName);
        List<State> states = parserFactory.getParser(extension).readValues(file, State.class);
        log.trace("{}", states);

        states.forEach(state -> {
            State byName = stateService.findByName(state.getName());
            if (byName == null) {
                byName = State.of(state.getName());
            }
            Map<String, Statistics> statisticsMap = byName.getStatistics().stream()
                    .collect(Collectors.toMap(Statistics::getYear, Function.identity()));

            state.getStatistics().forEach(statistic -> {
                        Statistics statistics = statisticsMap.get(statistic.getYear());
                        if (statistics == null) {
                            statistics = Statistics.builder()
                                    .year(statistic.getYear())
                                    .crimes(statistic.getCrimes())
                                    .population(statistic.getPopulation())
                                    .build();
                            statisticsMap.put(statistic.getYear(), statistics);
                        }
                    }
            );

            List<Statistics> statistics = new ArrayList<>();
            statisticsMap.forEach((s, statistics1) -> statistics.add(statistics1));
            byName.setStatistics(statistics);
            log.trace("{}", byName);
            stateService.save(byName);
        });
    }
}
