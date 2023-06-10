package com.pseudoorganization.pseudosystemsintegration.services.parsers;

import java.io.InputStream;
import java.util.List;

public interface Parser {
    <T> List<T> readValues(InputStream file, Class<T> clazz);
}
