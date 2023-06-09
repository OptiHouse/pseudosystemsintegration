package com.pseudoorganization.pseudosystemsintegration.services.export;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

@Component
public class YamlParser extends com.fasterxml.jackson.dataformat.yaml.YAMLMapper {

    public YamlParser() {
        super();
        this.registerModule(new JavaTimeModule());
    }
}
