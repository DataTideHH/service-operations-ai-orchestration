package de.datatidehh.serviceoperations.evaluation;

import java.util.List;

public record EvaluationComparison(
        boolean compatible,
        List<String> incompatibilities,
        List<String> regressions,
        List<String> improvements,
        List<String> unchanged) {

    public EvaluationComparison {
        incompatibilities = List.copyOf(incompatibilities);
        regressions = List.copyOf(regressions);
        improvements = List.copyOf(improvements);
        unchanged = List.copyOf(unchanged);
    }

    public boolean passed() {
        return compatible && regressions.isEmpty();
    }
}
