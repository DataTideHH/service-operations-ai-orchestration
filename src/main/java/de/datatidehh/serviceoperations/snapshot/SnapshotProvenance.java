package de.datatidehh.serviceoperations.snapshot;

import java.util.Map;

public record SnapshotProvenance(
        String producerApplication,
        String producerVersion,
        String sourceRepository,
        String sourceRevision,
        String scenario,
        String ingestionBatchId,
        Map<String, String> resourceFingerprints) {

    public SnapshotProvenance {
        resourceFingerprints = Map.copyOf(resourceFingerprints);
    }
}
