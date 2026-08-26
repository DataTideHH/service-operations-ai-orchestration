package de.datatidehh.serviceoperations.tool;

import java.util.List;

public record ServiceComparisonEvidence(
        String metric,
        String definition,
        String ordering,
        List<ServiceSlaEvidence> services) {

    public ServiceComparisonEvidence {
        services = List.copyOf(services);
    }
}
