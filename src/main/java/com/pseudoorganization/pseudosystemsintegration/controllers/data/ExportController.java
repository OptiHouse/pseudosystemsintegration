package com.pseudoorganization.pseudosystemsintegration.controllers.data;

import com.pseudoorganization.pseudosystemsintegration.models.State;
import com.pseudoorganization.pseudosystemsintegration.services.StateService;
import com.pseudoorganization.pseudosystemsintegration.services.export.ExportService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/export")
public class ExportController {

    private final StateService stateService;
    private final ExportService exportService;

    @GetMapping("json")
    @SneakyThrows
    public ResponseEntity<Resource> exportJSON(@RequestParam Optional<List<String>> states,
                                               @RequestParam Optional<List<String>> years,
                                               @RequestParam Optional<List<String>> crimes,
                                               @RequestParam Optional<List<String>> races) {
        List<State> filtered = stateService.getFiltered(states, years, crimes, races);
        String json = exportService.exportJSON(filtered);
        return prepareResponse("export.json", json);
    }

    @GetMapping("xml")
    @SneakyThrows
    public ResponseEntity<Resource> exportXML(@RequestParam Optional<List<String>> states,
                                              @RequestParam Optional<List<String>> years,
                                              @RequestParam Optional<List<String>> crimes,
                                              @RequestParam Optional<List<String>> races) {
        List<State> filtered = stateService.getFiltered(states, years, crimes, races);
        String xml = exportService.exportXML(filtered);
        return prepareResponse("export.xml", xml);
    }

    @GetMapping("yaml")
    @SneakyThrows
    public ResponseEntity<Resource> exportYAML(@RequestParam Optional<List<String>> states,
                                               @RequestParam Optional<List<String>> years,
                                               @RequestParam Optional<List<String>> crimes,
                                               @RequestParam Optional<List<String>> races) {
        List<State> filtered = stateService.getFiltered(states, years, crimes, races);
        String yaml = exportService.exportYAML(filtered);
        return prepareResponse("export.yaml", yaml);
    }

    private ResponseEntity<Resource> prepareResponse(String filename, String content) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(inputStream.available())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(filename)
                                .build()
                                .toString())
                .body(new ByteArrayResource(inputStream.readAllBytes()));
    }
}
