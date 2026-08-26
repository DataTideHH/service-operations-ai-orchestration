package de.datatidehh.serviceoperations.evaluation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationComparisonWriterTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void writesAnAuditableRegressionSummary() throws Exception {
        EvaluationRun baseline = EvaluationTestFixtures.run(true, true, true, true);
        EvaluationRun candidate = EvaluationTestFixtures.run(true, false, true, true);
        EvaluationComparison comparison = new EvaluationRunComparator().compare(baseline, candidate);

        Path report = new EvaluationComparisonWriter()
                .write(baseline, candidate, comparison, temporaryDirectory);

        assertThat(report.getFileName().toString()).isEqualTo("evaluation-comparison.md");
        assertThat(Files.readString(report))
                .contains("Result: **FAIL**")
                .contains("case-2");
    }
}
