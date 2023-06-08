package com.pseudoorganization.pseudosystemsintegration.controllers.data;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/export")
public class ExportController {

    @GetMapping("/export")
    public String exportFiles() {
        return "export";
    }
}
