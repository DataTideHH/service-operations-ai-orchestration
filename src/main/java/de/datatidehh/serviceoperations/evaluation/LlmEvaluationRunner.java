package de.datatidehh.serviceoperations.evaluation;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(name = "app.evaluation.enabled", havingValue = "true")
public class LlmEvaluationRunner implements CommandLineRunner {

    private final ChatClient answerClient;
    private final ChatClient judgeClient;
    private final EvaluationCatalog catalog;
    private final ToolInvocationRecorder invocationRecorder;
    private final EvaluationReportWriter reportWriter;
    private final EvaluationRunManifestFactory manifestFactory;
    private final EvaluationPromptCatalog prompts;
    private final String provider;
    private final String model;
    private final Path outputDirectory;

    public LlmEvaluationRunner(
            @Qualifier("serviceOperationsChatClient") ChatClient answerClient,
            @Qualifier("evaluationJudgeChatClient") ChatClient judgeClient,
            EvaluationCatalog catalog,
            ToolInvocationRecorder invocationRecorder,
            EvaluationReportWriter reportWriter,
            EvaluationRunManifestFactory manifestFactory,
            EvaluationPromptCatalog prompts,
            @Value("${app.evaluation.provider}") String provider,
            @Value("${app.evaluation.model}") String model,
            @Value("${app.evaluation.output-directory}") Path outputDirectory) {
        this.answerClient = answerClient;
        this.judgeClient = judgeClient;
        this.catalog = catalog;
        this.invocationRecorder = invocationRecorder;
        this.reportWriter = reportWriter;
        this.manifestFactory = manifestFactory;
        this.prompts = prompts;
        this.provider = provider;
        this.model = model;
        this.outputDirectory = outputDirectory;
    }

    @Override
    public void run(String... args) {
        List<EvaluationCaseResult> results = new ArrayList<>();
        for (EvaluationCase evaluationCase : catalog.cases()) {
            results.add(evaluate(evaluationCase));
        }

        Instant runAt = Instant.now();
        EvaluationRun run = new EvaluationRun(manifestFactory.create(provider, model, runAt), results);
        EvaluationReportArtifacts reports = reportWriter.write(run, outputDirectory);
        invocationRecorder.clear();
        System.out.printf("V3 evaluation: %d/%d passed. Reports: %s, %s%n",
                run.passedCount(), run.results().size(),
                reports.markdown().toAbsolutePath(), reports.json().toAbsolutePath());

        if (!run.passed()) {
            throw new IllegalStateException("One or more V3 evaluation cases failed; see " + reports.markdown());
        }
    }

    private EvaluationCaseResult evaluate(EvaluationCase evaluationCase) {
        invocationRecorder.reset();
        try {
            String answer = answerClient.prompt()
                    .user(evaluationCase.question())
                    .call()
                    .content();
            List<ToolInvocation> invocations = invocationRecorder.snapshot();
            EvaluationAssessment assessment = judge(evaluationCase, invocations, answer);
            return new EvaluationCaseResult(evaluationCase, invocations, answer, assessment);
        }
        catch (RuntimeException exception) {
            return new EvaluationCaseResult(
                    evaluationCase,
                    invocationRecorder.snapshot(),
                    "_Evaluation call failed before an answer was produced._",
                    EvaluationAssessment.executionFailure(exception.getMessage()));
        }
    }

    private EvaluationAssessment judge(
            EvaluationCase evaluationCase,
            List<ToolInvocation> invocations,
            String answer) {
        String prompt = prompts.renderJudgeCase(evaluationCase, invocations, answer);

        EvaluationAssessment assessment = judgeClient.prompt()
                .user(prompt)
                .call()
                .entity(EvaluationAssessment.class, spec -> spec.validateSchema());
        if (assessment == null) {
            throw new IllegalStateException("Judge returned no assessment");
        }
        return assessment;
    }
}
