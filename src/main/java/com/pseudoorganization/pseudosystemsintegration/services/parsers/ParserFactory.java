package com.pseudoorganization.pseudosystemsintegration.services.parsers;

import org.springframework.stereotype.Component;

@Component
public class ParserFactory {

    public Parser getParser(String fileExtension) {
        if (fileExtension == null) {
            return null;
        }
        if (fileExtension.equalsIgnoreCase("json")) {
            return new JsonParser();
        } else if (fileExtension.equalsIgnoreCase("xml")) {
            return new XmlParser();
        } else if (fileExtension.equalsIgnoreCase("yaml")) {
            return new YamlParser();
        }
        return null;
    }
}
