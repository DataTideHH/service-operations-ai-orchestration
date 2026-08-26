package de.datatidehh.serviceoperations.tool;

import java.util.List;

public record InterpretationBoundary(
        List<String> supportedInterpretations,
        List<String> unsupportedInterpretations,
        String requiredLanguage) {

    public InterpretationBoundary {
        supportedInterpretations = List.copyOf(supportedInterpretations);
        unsupportedInterpretations = List.copyOf(unsupportedInterpretations);
    }

    public static InterpretationBoundary governedSlaBoundary() {
        return new InterpretationBoundary(
                List.of(
                        "Describe the observed SLA attainment and breach rates in this snapshot.",
                        "Compare services using the same governed metric definition.",
                        "Identify the highest or lowest observed rate when the requested metric is explicit."),
                List.of(
                        "Infer root causes, drivers, or explanations for observed differences.",
                        "Predict future performance from this snapshot.",
                        "Infer individual or team quality, effort, or accountability.",
                        "Treat 'worst' as well-defined when the comparison metric is not stated."),
                "Use observational language such as 'the snapshot shows'; explicitly say when causality or an ambiguous ranking is not supported.");
    }
}
