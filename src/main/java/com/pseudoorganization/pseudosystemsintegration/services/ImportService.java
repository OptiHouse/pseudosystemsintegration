package com.pseudoorganization.pseudosystemsintegration.services;

import com.pseudoorganization.pseudosystemsintegration.models.State;
import com.pseudoorganization.pseudosystemsintegration.services.parsers.ParserFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImportService {

    private final ParserFactory parserFactory;
    private final StateService stateService;

    public void importFile(final InputStream file, final String fileName) {
        String extension = FilenameUtils.getExtension(fileName);
        List<State> states = parserFactory.getParser(extension).readValues(file, State.class);
        log.trace("{}", states);

        states.forEach(state -> {
            State byName = stateService.findByName(state.getName());
            if (byName == null) {
                byName = State.of(state.getName());
            }
            byName.setStatistics(state.getStatistics());
            stateService.save(byName);
        });
    }
}
