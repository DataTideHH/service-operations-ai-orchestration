package de.datatidehh.serviceoperations.evaluation;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class EvaluationComparisonWriter {

    public Path write(EvaluationRun baseline, EvaluationRun candidate, EvaluationComparison comparison, Path outputDirectory) {
        try {
            Files.createDirectories(outputDirectory);
            Path output = outputDirectory.resolve("evaluation-comparison.md");
            String markdown = """
                    # Governed evaluation comparison

                    - Baseline: `%s` / `%s` at `%s`
                    - Candidate: `%s` / `%s` at `%s`
                    - Compatible: `%s`
                    - Result: **%s**

                    ## Incompatibilities

                    %s

                    ## Regressions

                    %s

                    ## Improvements

                    %s

                    ## Unchanged

                    %s
                    """.formatted(
                    baseline.manifest().provider(), baseline.manifest().model(), baseline.manifest().runAt(),
                    candidate.manifest().provider(), candidate.manifest().model(), candidate.manifest().runAt(),
                    comparison.compatible(), comparison.passed() ? "PASS" : "FAIL",
                    list(comparison.incompatibilities()), list(comparison.regressions()),
                    list(comparison.improvements()), list(comparison.unchanged()));
            Files.writeString(output, markdown, StandardCharsets.UTF_8);
            return output;
        }
        catch (IOException exception) {
            throw new UncheckedIOException("Could not write evaluation comparison", exception);
        }
    }

    private static String list(java.util.List<String> values) {
        return values.isEmpty() ? "_None._" : values.stream().map(value -> "- " + value).collect(java.util.stream.Collectors.joining("\n"));
    }
}
