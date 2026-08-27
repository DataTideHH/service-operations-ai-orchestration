package de.datatidehh.serviceoperations.evaluation;

import de.datatidehh.serviceoperations.tool.GroupSlaEvidence;
import de.datatidehh.serviceoperations.tool.OverallSlaEvidence;
import de.datatidehh.serviceoperations.tool.ServiceComparisonEvidence;
import de.datatidehh.serviceoperations.tool.ServiceOperationsAnalyticsTools;
import de.datatidehh.serviceoperations.tool.ToolResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class OfflineEvaluationService {

    static final String PROVIDER = "offline-simulation";
    static final String MODEL = "deterministic-reference-v2";

    private final EvaluationCatalog catalog;
    private final ServiceOperationsAnalyticsTools analyticsTools;
    private final ToolInvocationRecorder invocationRecorder;
    private final EvaluationRunManifestFactory manifestFactory;

    public OfflineEvaluationService(
            EvaluationCatalog catalog,
            ServiceOperationsAnalyticsTools analyticsTools,
            ToolInvocationRecorder invocationRecorder,
            EvaluationRunManifestFactory manifestFactory) {
        this.catalog = catalog;
        this.analyticsTools = analyticsTools;
        this.invocationRecorder = invocationRecorder;
        this.manifestFactory = manifestFactory;
    }

    public EvaluationRun createRun(Instant runAt) {
        List<EvaluationCaseResult> results = new ArrayList<>();
        for (EvaluationCase evaluationCase : catalog.cases()) {
            results.add(evaluate(evaluationCase));
        }
        invocationRecorder.clear();
        return new EvaluationRun(manifestFactory.create(PROVIDER, MODEL, runAt), results);
    }

    private EvaluationCaseResult evaluate(EvaluationCase evaluationCase) {
        invocationRecorder.reset();
        String answer = switch (evaluationCase.id()) {
            case "01-overall-sla" -> overallAnswer();
            case "02-highest-breach-rate" -> highestBreachRateAnswer();
            case "03-ambiguous-worst" -> ambiguousWorstAnswer();
            case "04-causal-boundary" -> causalBoundaryAnswer();
            default -> throw new IllegalStateException("Unsupported offline evaluation case: " + evaluationCase.id());
        };
        return new EvaluationCaseResult(
                evaluationCase,
                invocationRecorder.snapshot(),
                answer,
                passingAssessment(evaluationCase.id()));
    }

    private String overallAnswer() {
        ToolResponse<OverallSlaEvidence> response = analyticsTools.getOverallSlaPerformance();
        OverallSlaEvidence evidence = response.evidence();
        return "The pinned %s snapshot shows %d of %d operations within SLA (%s%% attainment) and %d breached "
                .formatted(response.period(), evidence.withinSlaOperations(), evidence.eligibleOperations(),
                        evidence.slaAttainmentRatePercent(), evidence.breachedOperations())
                + "(%s%% breach) among SLA-eligible closed requests. It does not establish a trend, target, or cause."
                .formatted(evidence.slaBreachRatePercent());
    }

    private String highestBreachRateAnswer() {
        ToolResponse<ServiceComparisonEvidence> response = analyticsTools.compareServiceSlaPerformance();
        GroupSlaEvidence highest = response.evidence().groups().getFirst();
        return "The pinned %s snapshot shows %s with the highest observed assigned-team SLA breach rate: %s%% (%d of %d eligible closed requests breached). "
                .formatted(response.period(), highest.group(), highest.slaBreachRatePercent(),
                        highest.breachedOperations(), highest.eligibleOperations())
                + "The snapshot does not explain why this rate is highest.";
    }

    private String ambiguousWorstAnswer() {
        ToolResponse<ServiceComparisonEvidence> response = analyticsTools.compareServiceSlaPerformance();
        GroupSlaEvidence highest = response.evidence().groups().getFirst();
        return "'Worst' is underspecified without a comparison metric. Using the governed assigned-team SLA breach rate only, the pinned %s snapshot "
                .formatted(response.period())
                + "shows %s with the highest observed rate at %s%%; this is not a general team-quality judgment."
                .formatted(highest.group(), highest.slaBreachRatePercent());
    }

    private String causalBoundaryAnswer() {
        ToolResponse<ServiceComparisonEvidence> response = analyticsTools.compareServiceSlaPerformance();
        GroupSlaEvidence highest = response.evidence().groups().getFirst();
        return "The pinned %s snapshot cannot establish why %s had the highest assigned-team SLA breach rate. It only shows %d of %d eligible closed requests breached "
                .formatted(response.period(), highest.group(), highest.breachedOperations(), highest.eligibleOperations())
                + "(%s%%); staffing, complexity, demand, process, supplier, or team-performance explanations would be unsupported."
                .formatted(highest.slaBreachRatePercent());
    }

    private static EvaluationAssessment passingAssessment(String caseId) {
        return new EvaluationAssessment(
                true,
                List.of("expected governed tool used", "required metric semantics preserved", "interpretation boundary preserved"),
                List.of(),
                "Deterministic offline reference for %s satisfies the catalog expectation. This is a curated assessment, not an LLM judgment."
                        .formatted(caseId));
    }
}
