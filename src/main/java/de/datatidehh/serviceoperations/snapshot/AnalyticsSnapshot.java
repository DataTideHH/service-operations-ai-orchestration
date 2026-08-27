package de.datatidehh.serviceoperations.snapshot;

import java.time.LocalDate;
import java.util.List;

public record AnalyticsSnapshot(
        String schemaVersion,
        String snapshotId,
        String contractVersion,
        LocalDate asOfDate,
        String period,
        String metricDefinition,
        String metricEligiblePopulation,
        String comparisonDimension,
        SnapshotProvenance provenance,
        SnapshotBoundary interpretationBoundary,
        List<ComparisonGroupMetric> groups) {

    public AnalyticsSnapshot {
        groups = List.copyOf(groups);
        if (groups.isEmpty()) {
            throw new IllegalArgumentException("snapshot must contain at least one comparison group");
        }
    }

    public int eligibleOperations() {
        return groups.stream().mapToInt(ComparisonGroupMetric::eligibleOperations).sum();
    }

    public int withinSlaOperations() {
        return groups.stream().mapToInt(ComparisonGroupMetric::withinSlaOperations).sum();
    }
}
