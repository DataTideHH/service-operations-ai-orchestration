package de.datatidehh.serviceoperations.evaluation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.time.Instant;

@Component
@ConditionalOnProperty(name = "app.evaluation.artifact-command", havingValue = "simulate")
public class OfflineEvaluationRunner implements CommandLineRunner {

    private final OfflineEvaluationService evaluationService;
    private final EvaluationReportWriter reportWriter;
    private final Path outputDirectory;
    private final String runAt;

    public OfflineEvaluationRunner(
            OfflineEvaluationService evaluationService,
            EvaluationReportWriter reportWriter,
            @Value("${app.evaluation.output-directory}") Path outputDirectory,
            @Value("${app.evaluation.offline-run-at:now}") String runAt) {
        this.evaluationService = evaluationService;
        this.reportWriter = reportWriter;
        this.outputDirectory = outputDirectory;
        this.runAt = runAt;
    }

    @Override
    public void run(String... args) {
        Instant timestamp = "now".equalsIgnoreCase(runAt) ? Instant.now() : Instant.parse(runAt);
        EvaluationRun run = evaluationService.createRun(timestamp);
        EvaluationReportArtifacts reports = reportWriter.write(run, outputDirectory);
        System.out.printf("Offline simulation: %d/%d passed. Reports: %s, %s%n",
                run.passedCount(), run.results().size(),
                reports.markdown().toAbsolutePath(), reports.json().toAbsolutePath());
    }
}
