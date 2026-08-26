package de.datatidehh.serviceoperations.snapshot;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClasspathAnalyticsSnapshotRepositoryTest {

    private final AnalyticsSnapshot snapshot = new ClasspathAnalyticsSnapshotRepository().get();

    @Test
    void loadsPinnedContractAndReconciledTotals() {
        assertThat(snapshot.snapshotId()).isEqualTo("fsoa-2026-q2-v1");
        assertThat(snapshot.contractVersion()).isEqualTo("1.0.0");
        assertThat(snapshot.handledOperations()).isEqualTo(1_000);
        assertThat(snapshot.withinSlaOperations()).isEqualTo(920);
        assertThat(snapshot.services()).hasSize(3);
    }

    @Test
    void derivesRatesFromCountsInsteadOfStoringConflictingValues() {
        ServiceMetric billing = snapshot.services().stream()
                .filter(metric -> metric.service().equals("billing"))
                .findFirst()
                .orElseThrow();

        assertThat(billing.breachedOperations()).isEqualTo(38);
        assertThat(billing.slaAttainmentRatePercent()).isEqualByComparingTo("84.80");
        assertThat(billing.slaBreachRatePercent()).isEqualByComparingTo("15.20");
    }
}
