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
        assertThat(markdown).contains("# Governed LLM evaluation report");
        assertThat(markdown).contains("### Observed answer");
        assertThat(markdown).contains("### Automatic assessment");
        assertThat(markdown).contains("Governed resource fingerprints");
        assertThat(markdown).contains("`expected_tool`");
        assertThat(markdown).contains("causal claim");
        assertThat(markdown).contains("Observed answer 1.");
        assertThat(validator.readAndValidate(reports.json())).isEqualTo(run);
    }

    @Test
    void labelsOfflineEvidenceAsAuthoredReferenceMaterial() {
        EvaluationJsonCodec codec = new EvaluationJsonCodec();
        EvaluationReportValidator validator = new EvaluationReportValidator(codec);
        EvaluationRun providerRun = EvaluationTestFixtures.run(true, true, true, true);
        EvaluationRunManifest manifest = providerRun.manifest();
        EvaluationRun offlineRun = new EvaluationRun(
                new EvaluationRunManifest(
                        manifest.schemaVersion(),
                        manifest.applicationVersion(),
                        OfflineEvaluationService.PROVIDER,
                        OfflineEvaluationService.MODEL,
                        manifest.sourceRevision(),
                        manifest.runAt(),
                        manifest.promptVersions(),
                        manifest.resourceFingerprints()),
                providerRun.results());

        String markdown = new EvaluationReportWriter(codec, validator).render(offlineRun);

        assertThat(markdown)
                .contains("Offline evaluation pipeline report")
                .contains("Pipeline check: **4/4 cases executed — no model involved**")
                .contains("Reference answer — authored, not model-generated")
                .contains("Deterministic reference assessment")
                .doesNotContain("### Observed answer")
                .doesNotContain("### Automatic assessment");
    }
}
