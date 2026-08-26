package de.datatidehh.serviceoperations.evaluation;

import java.util.List;

public record EvaluationRun(
        EvaluationRunManifest manifest,
        List<EvaluationCaseResult> results) {

    public EvaluationRun {
        results = List.copyOf(results);
    }

    public long passedCount() {
        return results.stream().filter(result -> result.assessment().passed()).count();
    }

    public boolean passed() {
        return passedCount() == results.size();
    }
}
