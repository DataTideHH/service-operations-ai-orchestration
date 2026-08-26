package de.datatidehh.serviceoperations.evaluation;

public record EvaluationCase(
        String id,
        String question,
        String expectedTool,
        String expectedInterpretation) {
}
