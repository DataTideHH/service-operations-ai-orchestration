package de.datatidehh.serviceoperations.snapshot;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClasspathAnalyticsSnapshotRepositoryTest {

    private final AnalyticsSnapshot snapshot = new ClasspathAnalyticsSnapshotRepository().get();

    @Test
    void loadsPinnedContractAndReconciledTotals() {
        assertThat(snapshot.schemaVersion()).isEqualTo("1.0.0");
        assertThat(snapshot.snapshotId()).isEqualTo("fsoa-2026-01-01-2026-03-31-v1");
        assertThat(snapshot.contractVersion()).isEqualTo("2.0.0");
        assertThat(snapshot.eligibleOperations()).isEqualTo(833);
        assertThat(snapshot.withinSlaOperations()).isEqualTo(799);
        assertThat(snapshot.comparisonDimension()).isEqualTo("assigned_team");
        assertThat(snapshot.groups()).hasSize(5);
        assertThat(snapshot.provenance().sourceRevision())
                .isEqualTo("0a6c4ebffe366d7133215634d836a5d9b102e7fb");
        assertThat(snapshot.provenance().resourceFingerprints()).hasSize(3);
    }

    @Test
    void derivesRatesFromCountsInsteadOfStoringConflictingValues() {
        ComparisonGroupMetric networkOps = snapshot.groups().stream()
                .filter(metric -> metric.group().equals("network_ops"))
                .findFirst()
                .orElseThrow();

        assertThat(networkOps.breachedOperations()).isEqualTo(9);
        assertThat(networkOps.slaAttainmentRatePercent()).isEqualByComparingTo("93.02");
        assertThat(networkOps.slaBreachRatePercent()).isEqualByComparingTo("6.98");
    }
}
