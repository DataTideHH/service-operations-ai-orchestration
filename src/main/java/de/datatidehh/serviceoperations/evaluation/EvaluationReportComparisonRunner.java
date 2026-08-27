package de.datatidehh.serviceoperations.evaluation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@ConditionalOnProperty(name = "app.evaluation.artifact-command", havingValue = "compare")
public class EvaluationReportComparisonRunner implements CommandLineRunner {

    private final EvaluationReportValidator validator;
    private final EvaluationRunComparator comparator;
    private final EvaluationComparisonWriter writer;
    private final Path baselineReport;
    private final Path candidateReport;
    private final Path outputDirectory;

    public EvaluationReportComparisonRunner(
            EvaluationReportValidator validator,
            EvaluationRunComparator comparator,
            EvaluationComparisonWriter writer,
            @Value("${app.evaluation.artifact-baseline}") Path baselineReport,
            @Value("${app.evaluation.artifact-report}") Path candidateReport,
            @Value("${app.evaluation.output-directory}") Path outputDirectory) {
        this.validator = validator;
        this.comparator = comparator;
        this.writer = writer;
        this.baselineReport = baselineReport;
        this.candidateReport = candidateReport;
        this.outputDirectory = outputDirectory;
    }

    @Override
    public void run(String... args) {
        EvaluationRun baseline = validator.readAndValidate(baselineReport);
        EvaluationRun candidate = validator.readAndValidate(candidateReport);
        EvaluationComparison comparison = comparator.compare(baseline, candidate);
        Path output = writer.write(baseline, candidate, comparison, outputDirectory);
        System.out.printf("Governed comparison: %s. Report: %s%n",
                comparison.passed() ? "PASS" : "FAIL", output.toAbsolutePath());
        if (!comparison.passed()) {
            throw new IllegalStateException("Evaluation comparison contains regressions or incompatible evidence; see " + output);
        }
    }
}
