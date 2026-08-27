package de.datatidehh.serviceoperations.snapshot;

import java.util.List;

public record SnapshotBoundary(
        List<String> supportedInterpretations,
        List<String> unsupportedInterpretations,
        String requiredLanguage) {

    public SnapshotBoundary {
        supportedInterpretations = List.copyOf(supportedInterpretations);
        unsupportedInterpretations = List.copyOf(unsupportedInterpretations);
    }
}
