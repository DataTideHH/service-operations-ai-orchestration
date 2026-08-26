package de.datatidehh.serviceoperations.tool;

import java.time.LocalDate;

public record ToolResponse<T>(
        String contractVersion,
        String snapshotId,
        LocalDate asOfDate,
        String period,
        T evidence,
        InterpretationBoundary interpretationBoundary) {
}
