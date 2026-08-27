package de.datatidehh.serviceoperations.snapshot;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record ComparisonGroupMetric(
        String group,
        int eligibleOperations,
        int withinSlaOperations) {

    public ComparisonGroupMetric {
        if (group == null || group.isBlank()) {
            throw new IllegalArgumentException("group must not be blank");
        }
        if (eligibleOperations <= 0) {
            throw new IllegalArgumentException("eligibleOperations must be positive");
        }
        if (withinSlaOperations < 0 || withinSlaOperations > eligibleOperations) {
            throw new IllegalArgumentException("withinSlaOperations must be between zero and eligibleOperations");
        }
    }

    public int breachedOperations() {
        return eligibleOperations - withinSlaOperations;
    }

    public BigDecimal slaAttainmentRatePercent() {
        return percent(withinSlaOperations);
    }

    public BigDecimal slaBreachRatePercent() {
        return percent(breachedOperations());
    }

    private BigDecimal percent(int value) {
        return BigDecimal.valueOf(value)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(eligibleOperations), 2, RoundingMode.HALF_UP);
    }
}
