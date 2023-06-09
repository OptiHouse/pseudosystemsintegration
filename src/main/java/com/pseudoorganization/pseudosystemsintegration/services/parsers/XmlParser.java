package com.pseudoorganization.pseudosystemsintegration.services.parsers;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class XmlParser extends com.fasterxml.jackson.dataformat.xml.XmlMapper implements Parser {

    public XmlParser() {
        super();
        this.configure(com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        this.registerModule(new JavaTimeModule());
    }

    @SneakyThrows
    @Override
    public <T> List<T> readValues(InputStream file, Class<T> clazz) {
        return this.readValue(file, this.getTypeFactory().constructCollectionType(List.class, clazz));
    }
}
