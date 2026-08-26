package de.datatidehh.serviceoperations.snapshot;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record ServiceMetric(String service, int handledOperations, int withinSlaOperations) {

    public ServiceMetric {
        if (service == null || service.isBlank()) {
            throw new IllegalArgumentException("service must not be blank");
        }
        if (handledOperations <= 0) {
            throw new IllegalArgumentException("handledOperations must be positive");
        }
        if (withinSlaOperations < 0 || withinSlaOperations > handledOperations) {
            throw new IllegalArgumentException("withinSlaOperations must be between zero and handledOperations");
        }
    }

    public int breachedOperations() {
        return handledOperations - withinSlaOperations;
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
                .divide(BigDecimal.valueOf(handledOperations), 2, RoundingMode.HALF_UP);
    }
}
