package com.pseudoorganization.pseudosystemsintegration.services.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class JsonParser extends ObjectMapper {
    public JsonParser() {
        super();
        this.registerModule(new JavaTimeModule());
    }
}
