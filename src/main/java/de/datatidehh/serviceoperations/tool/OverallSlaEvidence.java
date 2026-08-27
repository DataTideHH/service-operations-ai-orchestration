package de.datatidehh.serviceoperations.tool;

import java.math.BigDecimal;

public record OverallSlaEvidence(
        String metric,
        String definition,
        String eligiblePopulation,
        int eligibleOperations,
        int withinSlaOperations,
        int breachedOperations,
        BigDecimal slaAttainmentRatePercent,
        BigDecimal slaBreachRatePercent) {
}
