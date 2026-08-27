package de.datatidehh.serviceoperations.tool;

import de.datatidehh.serviceoperations.snapshot.SnapshotProvenance;

import java.time.LocalDate;

public record ToolResponse<T>(
        String contractVersion,
        String snapshotId,
        LocalDate asOfDate,
        String period,
        SnapshotProvenance provenance,
        T evidence,
        InterpretationBoundary interpretationBoundary) {
}
