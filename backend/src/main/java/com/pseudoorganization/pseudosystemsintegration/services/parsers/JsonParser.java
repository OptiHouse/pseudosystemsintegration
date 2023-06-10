package com.pseudoorganization.pseudosystemsintegration.services.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Primary
@Component
public class JsonParser extends ObjectMapper implements Parser {
    public JsonParser() {
        super();
        this.registerModule(new JavaTimeModule());
    }

    @SneakyThrows
    @Override
    public <T> List<T> readValues(InputStream file, Class<T> clazz) {
        return this.readValue(file, this.getTypeFactory().constructCollectionType(ArrayList.class, clazz));
    }
}
