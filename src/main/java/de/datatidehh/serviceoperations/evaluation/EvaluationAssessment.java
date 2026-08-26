package de.datatidehh.serviceoperations.evaluation;

import java.util.List;

public record EvaluationAssessment(
        boolean passed,
        List<String> satisfiedCriteria,
        List<String> violations,
        String rationale) {

    public EvaluationAssessment {
        satisfiedCriteria = satisfiedCriteria == null ? List.of() : List.copyOf(satisfiedCriteria);
        violations = violations == null ? List.of() : List.copyOf(violations);
        rationale = rationale == null ? "" : rationale;
    }

    public static EvaluationAssessment executionFailure(String message) {
        return new EvaluationAssessment(false, List.of(), List.of("evaluation execution failed"), message);
    }
}
