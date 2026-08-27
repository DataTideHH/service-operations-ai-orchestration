package de.datatidehh.serviceoperations.evaluation;

import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class EvaluationReportValidator {

    private static final int EXPECTED_CASE_COUNT = 4;
    private static final List<String> V3_GOVERNED_RESOURCE_PATHS = List.of(
            "analytics/service-operations-snapshot-v1.properties",
            "evaluation/evaluation-cases-v2.properties",
            EvaluationPromptCatalog.ANSWER_SYSTEM_PATH,
            EvaluationPromptCatalog.JUDGE_SYSTEM_PATH,
            EvaluationPromptCatalog.JUDGE_CASE_PATH);
    private final EvaluationJsonCodec jsonCodec;

    public EvaluationReportValidator(EvaluationJsonCodec jsonCodec) {
        this.jsonCodec = jsonCodec;
    }

    public EvaluationRun readAndValidate(Path report) {
        EvaluationRun run = jsonCodec.read(report);
        validate(run);
        return run;
    }

    public void validate(EvaluationRun run) {
        Set<String> violations = new LinkedHashSet<>();
        EvaluationRunManifest manifest = run.manifest();
        if (manifest == null) {
            throw new IllegalArgumentException("Evaluation report has no manifest");
        }
        require(manifest.schemaVersion(), "manifest.schemaVersion", violations);
        if (!EvaluationRunManifestFactory.SCHEMA_VERSION.equals(manifest.schemaVersion())) {
            violations.add("unsupported schema version: " + manifest.schemaVersion());
        }
        require(manifest.applicationVersion(), "manifest.applicationVersion", violations);
        require(manifest.provider(), "manifest.provider", violations);
        require(manifest.model(), "manifest.model", violations);
        require(manifest.sourceRevision(), "manifest.sourceRevision", violations);
        if (manifest.runAt() == null) {
            violations.add("manifest.runAt is required");
        }
        for (String prompt : EvaluationRunManifestFactory.PROMPT_VERSIONS.keySet()) {
            require(manifest.promptVersions().get(prompt), "manifest.promptVersions." + prompt, violations);
        }
        List<String> requiredResources = manifest.applicationVersion().startsWith("0.3.")
                ? V3_GOVERNED_RESOURCE_PATHS
                : EvaluationRunManifestFactory.GOVERNED_RESOURCE_PATHS;
        for (String path : requiredResources) {
            String fingerprint = manifest.resourceFingerprints().get(path);
            if (fingerprint == null || !fingerprint.matches("[0-9a-f]{64}")) {
                violations.add("missing or invalid SHA-256 fingerprint: " + path);
            }
        }
        manifest.resourceFingerprints().forEach((path, fingerprint) -> {
            if (path == null || path.isBlank() || fingerprint == null || !fingerprint.matches("[0-9a-f]{64}")) {
                violations.add("resource fingerprints must have non-blank paths and valid SHA-256 values");
            }
        });
        if (run.results().size() != EXPECTED_CASE_COUNT) {
            violations.add("expected " + EXPECTED_CASE_COUNT + " cases but found " + run.results().size());
        }
        Set<String> ids = new HashSet<>();
        for (EvaluationCaseResult result : run.results()) {
            if (result.evaluationCase() == null || !ids.add(result.evaluationCase().id())) {
                violations.add("case identifiers must be present and unique");
            }
            if (result.assessment() == null) {
                violations.add("every case requires an assessment");
            }
        }
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Invalid evaluation report: " + String.join("; ", violations));
        }
    }

    private static void require(String value, String field, Set<String> violations) {
        if (value == null || value.isBlank()) {
            violations.add(field + " is required");
        }
    }
}
