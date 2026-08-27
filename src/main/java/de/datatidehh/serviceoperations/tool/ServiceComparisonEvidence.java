package de.datatidehh.serviceoperations.tool;

import java.util.List;

public record ServiceComparisonEvidence(
        String metric,
        String definition,
        String eligiblePopulation,
        String comparisonDimension,
        String ordering,
        List<GroupSlaEvidence> groups) {

    public ServiceComparisonEvidence {
        groups = List.copyOf(groups);
    }
}
