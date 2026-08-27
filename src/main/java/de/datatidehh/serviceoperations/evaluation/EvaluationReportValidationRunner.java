package de.datatidehh.serviceoperations.evaluation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@ConditionalOnProperty(name = "app.evaluation.artifact-command", havingValue = "validate")
public class EvaluationReportValidationRunner implements CommandLineRunner {

    private final EvaluationReportValidator validator;
    private final Path report;

    public EvaluationReportValidationRunner(
            EvaluationReportValidator validator,
            @Value("${app.evaluation.artifact-report}") Path report) {
        this.validator = validator;
        this.report = report;
    }

    @Override
    public void run(String... args) {
        EvaluationRun run = validator.readAndValidate(report);
        System.out.printf("Valid governed report: %s (%d/%d passed).%n",
                report.toAbsolutePath(), run.passedCount(), run.results().size());
    }
}
