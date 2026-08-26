package de.datatidehh.serviceoperations.evaluation;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class EvaluationReportWriter {

    public Path write(EvaluationRun run, Path outputDirectory) {
        try {
            Files.createDirectories(outputDirectory);
            Path report = outputDirectory.resolve("evaluation-report.md");
            Files.writeString(report, render(run), StandardCharsets.UTF_8);
            return report;
        }
        catch (IOException exception) {
            throw new UncheckedIOException("Could not write evaluation report", exception);
        }
    }

    String render(EvaluationRun run) {
        StringBuilder markdown = new StringBuilder()
                .append("# V2 LLM evaluation report\n\n")
                .append("- Provider: `").append(run.provider()).append("`\n")
                .append("- Model: `").append(run.model()).append("`\n")
                .append("- Run at: `").append(run.runAt()).append("`\n")
                .append("- Result: **").append(run.passed() ? "PASS" : "FAIL").append("** (")
                .append(run.passedCount()).append('/').append(run.results().size()).append(")\n\n");

        for (EvaluationCaseResult result : run.results()) {
            EvaluationCase evaluationCase = result.evaluationCase();
            markdown.append("## ").append(evaluationCase.id()).append(" — ")
                    .append(result.assessment().passed() ? "PASS" : "FAIL").append("\n\n")
                    .append("**Question:** ").append(evaluationCase.question()).append("\n\n")
                    .append("**Expected tool:** `").append(evaluationCase.expectedTool()).append("`\n\n")
                    .append("**Expected interpretation:** ").append(evaluationCase.expectedInterpretation()).append("\n\n")
                    .append("### Observed tool calls\n\n");

            if (result.toolInvocations().isEmpty()) {
                markdown.append("_No tool invocation captured._\n\n");
            }
            else {
                for (ToolInvocation invocation : result.toolInvocations()) {
                    markdown.append("- `").append(invocation.toolName()).append("`\n\n")
                            .append("```text\n").append(invocation.result()).append("\n```\n\n");
                }
            }

            markdown.append("### Observed answer\n\n")
                    .append(result.observedAnswer()).append("\n\n")
                    .append("### Automatic assessment\n\n")
                    .append("- Passed: `").append(result.assessment().passed()).append("`\n")
                    .append("- Rationale: ").append(result.assessment().rationale()).append("\n")
                    .append("- Satisfied criteria: ").append(result.assessment().satisfiedCriteria()).append("\n")
                    .append("- Violations: ").append(result.assessment().violations()).append("\n\n");
        }
        return markdown.toString();
    }
}
