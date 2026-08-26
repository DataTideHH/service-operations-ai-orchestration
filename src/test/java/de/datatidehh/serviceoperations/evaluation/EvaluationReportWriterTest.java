package de.datatidehh.serviceoperations.evaluation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.assertThat;

class EvaluationReportWriterTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void writesAuditableFailingReportBeforeTheRunnerSignalsFailure() throws Exception {
        EvaluationJsonCodec codec = new EvaluationJsonCodec();
        EvaluationReportValidator validator = new EvaluationReportValidator(codec);
        EvaluationRun run = EvaluationTestFixtures.run(false, true, true, true);

        EvaluationReportArtifacts reports = new EvaluationReportWriter(codec, validator).write(run, temporaryDirectory);
        String markdown = Files.readString(reports.markdown());

        assertThat(reports.markdown().getFileName().toString()).isEqualTo("evaluation-report.md");
        assertThat(reports.json().getFileName().toString()).isEqualTo("evaluation-report.json");
        assertThat(markdown).contains("Result: **FAIL** (3/4)");
        assertThat(markdown).contains("Governed resource fingerprints");
        assertThat(markdown).contains("`expected_tool`");
        assertThat(markdown).contains("causal claim");
        assertThat(markdown).contains("Observed answer 1.");
        assertThat(validator.readAndValidate(reports.json())).isEqualTo(run);
    }
}
