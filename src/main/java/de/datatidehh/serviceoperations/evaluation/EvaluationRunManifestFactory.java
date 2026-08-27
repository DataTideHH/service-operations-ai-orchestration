package de.datatidehh.serviceoperations.evaluation;

import de.datatidehh.serviceoperations.snapshot.ClasspathAnalyticsSnapshotRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
public class EvaluationRunManifestFactory {

    public static final String SCHEMA_VERSION = "3.0.0";
    public static final Map<String, String> PROMPT_VERSIONS = Map.of(
            "answer-system", EvaluationPromptCatalog.ANSWER_SYSTEM_VERSION,
            "judge-system", EvaluationPromptCatalog.JUDGE_SYSTEM_VERSION,
            "judge-case", EvaluationPromptCatalog.JUDGE_CASE_VERSION);
    public static final List<String> GOVERNED_RESOURCE_PATHS = List.of(
            ClasspathAnalyticsSnapshotRepository.DEFAULT_LOCATION,
            ClasspathAnalyticsSnapshotRepository.DEFAULT_SCHEMA_LOCATION,
            EvaluationCatalog.LOCATION,
            EvaluationPromptCatalog.ANSWER_SYSTEM_PATH,
            EvaluationPromptCatalog.JUDGE_SYSTEM_PATH,
            EvaluationPromptCatalog.JUDGE_CASE_PATH);

    private final ResourceFingerprintService fingerprints;
    private final String applicationVersion;
    private final String sourceRevision;

    public EvaluationRunManifestFactory(
            ResourceFingerprintService fingerprints,
            @Value("${app.evaluation.application-version}") String applicationVersion,
            @Value("${app.evaluation.source-revision}") String sourceRevision) {
        this.fingerprints = fingerprints;
        this.applicationVersion = applicationVersion;
        this.sourceRevision = sourceRevision;
    }

    public EvaluationRunManifest create(String provider, String model, Instant runAt) {
        return new EvaluationRunManifest(
                SCHEMA_VERSION,
                applicationVersion,
                provider,
                model,
                sourceRevision,
                runAt,
                PROMPT_VERSIONS,
                fingerprints.sha256(GOVERNED_RESOURCE_PATHS));
    }
}
