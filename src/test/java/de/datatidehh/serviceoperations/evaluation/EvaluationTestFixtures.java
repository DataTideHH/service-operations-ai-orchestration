package de.datatidehh.serviceoperations.evaluation;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

final class EvaluationTestFixtures {

    private EvaluationTestFixtures() {
    }

    static EvaluationRun run(boolean... passed) {
        if (passed.length != 4) {
            throw new IllegalArgumentException("Exactly four case outcomes are required");
        }
        List<EvaluationCaseResult> results = IntStream.range(0, passed.length)
                .mapToObj(index -> result(index + 1, passed[index]))
                .toList();
        return new EvaluationRun(manifest(fingerprints()), results);
    }

    static EvaluationRunManifest manifest(Map<String, String> fingerprints) {
        return new EvaluationRunManifest(
                EvaluationRunManifestFactory.SCHEMA_VERSION,
                "0.4.0",
                "test-provider",
                "test-model",
                "test-revision",
                Instant.parse("2026-08-26T20:00:00Z"),
                EvaluationRunManifestFactory.PROMPT_VERSIONS,
                fingerprints);
    }

    static Map<String, String> fingerprints() {
        Map<String, String> fingerprints = new LinkedHashMap<>();
        EvaluationRunManifestFactory.GOVERNED_RESOURCE_PATHS.forEach(path -> fingerprints.put(path, "a".repeat(64)));
        return fingerprints;
    }

    private static EvaluationCaseResult result(int number, boolean passed) {
        String id = "case-" + number;
        EvaluationCase evaluationCase = new EvaluationCase(
                id, "Question " + number + "?", "expected_tool", "Preserve the boundary.");
        EvaluationAssessment assessment = new EvaluationAssessment(
                passed,
                passed ? List.of("metric retained") : List.of(),
                passed ? List.of() : List.of("causal claim"),
                passed ? "Boundary retained." : "Boundary violated.");
        return new EvaluationCaseResult(
                evaluationCase,
                List.of(new ToolInvocation("expected_tool", "governed result")),
                "Observed answer " + number + ".",
                assessment);
    }
}
