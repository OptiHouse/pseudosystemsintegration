package com.pseudoorganization.pseudosystemsintegration.services.export;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

@Component
public class XmlParser extends com.fasterxml.jackson.dataformat.xml.XmlMapper {

    public XmlParser() {
        super();
        this.configure(com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        this.registerModule(new JavaTimeModule());
    }
}
