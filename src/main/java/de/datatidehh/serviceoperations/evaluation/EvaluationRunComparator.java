package de.datatidehh.serviceoperations.evaluation;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class EvaluationRunComparator {

    public EvaluationComparison compare(EvaluationRun baseline, EvaluationRun candidate) {
        List<String> incompatibilities = new ArrayList<>();
        if (!baseline.manifest().resourceFingerprints().equals(candidate.manifest().resourceFingerprints())) {
            incompatibilities.add("governed resource fingerprints differ");
        }
        if (!baseline.manifest().promptVersions().equals(candidate.manifest().promptVersions())) {
            incompatibilities.add("prompt versions differ");
        }

        Map<String, EvaluationCaseResult> candidateById = new LinkedHashMap<>();
        candidate.results().forEach(result -> candidateById.put(result.evaluationCase().id(), result));
        List<String> regressions = new ArrayList<>();
        List<String> improvements = new ArrayList<>();
        List<String> unchanged = new ArrayList<>();

        for (EvaluationCaseResult baselineResult : baseline.results()) {
            String id = baselineResult.evaluationCase().id();
            EvaluationCaseResult candidateResult = candidateById.remove(id);
            if (candidateResult == null) {
                incompatibilities.add("candidate is missing case: " + id);
                continue;
            }
            boolean before = baselineResult.assessment().passed();
            boolean after = candidateResult.assessment().passed();
            if (before && !after) {
                regressions.add(id);
            }
            else if (!before && after) {
                improvements.add(id);
            }
            else {
                unchanged.add(id + ": " + (after ? "PASS" : "FAIL"));
            }
        }
        candidateById.keySet().forEach(id -> incompatibilities.add("candidate contains unexpected case: " + id));

        return new EvaluationComparison(
                incompatibilities.isEmpty(),
                incompatibilities,
                regressions,
                improvements,
                unchanged);
    }
}
