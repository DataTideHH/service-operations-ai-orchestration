package de.datatidehh.serviceoperations.evaluation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

@Component
public class EvaluationJsonCodec {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public String write(EvaluationRun run) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(run) + System.lineSeparator();
        }
        catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not serialize evaluation report", exception);
        }
    }

    public EvaluationRun read(Path report) {
        try {
            return objectMapper.readValue(report.toFile(), EvaluationRun.class);
        }
        catch (IOException exception) {
            throw new UncheckedIOException("Could not read evaluation report: " + report, exception);
        }
    }
}
