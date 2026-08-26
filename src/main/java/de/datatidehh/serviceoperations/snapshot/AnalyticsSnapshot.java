package de.datatidehh.serviceoperations.snapshot;

import java.time.LocalDate;
import java.util.List;

public record AnalyticsSnapshot(
        String snapshotId,
        String contractVersion,
        LocalDate asOfDate,
        String period,
        String metricDefinition,
        List<ServiceMetric> services) {

    public AnalyticsSnapshot {
        services = List.copyOf(services);
        if (services.isEmpty()) {
            throw new IllegalArgumentException("snapshot must contain at least one service");
        }
    }

    public int handledOperations() {
        return services.stream().mapToInt(ServiceMetric::handledOperations).sum();
    }

    public int withinSlaOperations() {
        return services.stream().mapToInt(ServiceMetric::withinSlaOperations).sum();
    }
}
