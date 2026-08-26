package de.datatidehh.serviceoperations.evaluation;

import java.util.List;

public record EvaluationCaseResult(
        EvaluationCase evaluationCase,
        List<ToolInvocation> toolInvocations,
        String observedAnswer,
        EvaluationAssessment assessment) {

    public EvaluationCaseResult {
        toolInvocations = List.copyOf(toolInvocations);
    }
}
