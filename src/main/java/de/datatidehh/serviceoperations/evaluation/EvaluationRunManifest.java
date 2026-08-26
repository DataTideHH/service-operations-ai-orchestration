package de.datatidehh.serviceoperations.evaluation;

import java.time.Instant;
import java.util.Map;

public record EvaluationRunManifest(
        String schemaVersion,
        String applicationVersion,
        String provider,
        String model,
        String sourceRevision,
        Instant runAt,
        Map<String, String> promptVersions,
        Map<String, String> resourceFingerprints) {

    public EvaluationRunManifest {
        promptVersions = Map.copyOf(promptVersions);
        resourceFingerprints = Map.copyOf(resourceFingerprints);
    }
}
