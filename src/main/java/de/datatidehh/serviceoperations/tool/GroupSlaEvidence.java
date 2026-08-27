package de.datatidehh.serviceoperations.tool;

import java.math.BigDecimal;

public record GroupSlaEvidence(
        String group,
        int eligibleOperations,
        int withinSlaOperations,
        int breachedOperations,
        BigDecimal slaAttainmentRatePercent,
        BigDecimal slaBreachRatePercent) {
}
