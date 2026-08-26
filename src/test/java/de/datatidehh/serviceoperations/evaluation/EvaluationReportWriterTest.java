package de.datatidehh.serviceoperations.evaluation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationReportWriterTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void writesAuditableFailingReportBeforeTheRunnerSignalsFailure() throws Exception {
        EvaluationCase evaluationCase = new EvaluationCase(
                "case-1", "Question?", "expected_tool", "Preserve the boundary.");
        EvaluationAssessment assessment = new EvaluationAssessment(
                false, List.of("metric retained"), List.of("causal claim"), "Boundary violated.");
        EvaluationRun run = new EvaluationRun(
                "test-provider",
                "test-model",
                Instant.parse("2026-08-26T20:00:00Z"),
                List.of(new EvaluationCaseResult(
                        evaluationCase,
                        List.of(new ToolInvocation("expected_tool", "governed result")),
                        "Observed answer.",
                        assessment)));

        Path report = new EvaluationReportWriter().write(run, temporaryDirectory);
        String markdown = Files.readString(report);

        assertThat(report.getFileName().toString()).isEqualTo("evaluation-report.md");
        assertThat(markdown).contains("Result: **FAIL** (0/1)");
        assertThat(markdown).contains("`expected_tool`");
        assertThat(markdown).contains("causal claim");
        assertThat(markdown).contains("Observed answer.");
    }
}
