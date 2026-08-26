package de.datatidehh.serviceoperations.tool;

import java.math.BigDecimal;

public record ServiceSlaEvidence(
        String service,
        int handledOperations,
        int withinSlaOperations,
        int breachedOperations,
        BigDecimal slaAttainmentRatePercent,
        BigDecimal slaBreachRatePercent) {
}
