package de.datatidehh.serviceoperations.evaluation;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Component
public class EvaluationCatalog {

    static final String LOCATION = "evaluation/evaluation-cases-v4.properties";

    public List<EvaluationCase> cases() {
        Properties properties = new Properties();
        try (InputStream input = new ClassPathResource(LOCATION).getInputStream()) {
            properties.load(input);
        }
        catch (IOException exception) {
            throw new UncheckedIOException("Could not load versioned evaluation catalog", exception);
        }

        return Arrays.stream(required(properties, "cases").split(","))
                .map(String::trim)
                .map(id -> new EvaluationCase(
                        id,
                        required(properties, "case." + id + ".question"),
                        required(properties, "case." + id + ".expected-tool"),
                        required(properties, "case." + id + ".expected-interpretation")))
                .toList();
    }

    private static String required(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing evaluation property: " + key);
        }
        return value.trim();
    }
}
