package com.pseudoorganization.pseudosystemsintegration.controllers.data;

import com.pseudoorganization.pseudosystemsintegration.services.ImportService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin()
@RequiredArgsConstructor
@RestController
@RequestMapping("/import")
public class ImportController {

    private final ImportService importService;

    @SneakyThrows
    @GetMapping("")
    public String importFiles(@RequestParam("file") MultipartFile file) {
        importService.importFile(file.getInputStream(), file.getOriginalFilename());
        return "imported";
    }
}
