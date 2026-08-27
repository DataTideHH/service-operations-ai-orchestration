package de.datatidehh.serviceoperations.tool;

import de.datatidehh.serviceoperations.snapshot.AnalyticsSnapshot;
import de.datatidehh.serviceoperations.snapshot.AnalyticsSnapshotRepository;
import de.datatidehh.serviceoperations.snapshot.ComparisonGroupMetric;
import de.datatidehh.serviceoperations.evaluation.ToolInvocationRecorder;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@Component
public class ServiceOperationsAnalyticsTools {

    private final AnalyticsSnapshotRepository snapshotRepository;
    private final ToolInvocationRecorder invocationRecorder;

    public ServiceOperationsAnalyticsTools(
            AnalyticsSnapshotRepository snapshotRepository,
            ToolInvocationRecorder invocationRecorder) {
        this.snapshotRepository = snapshotRepository;
        this.invocationRecorder = invocationRecorder;
    }

    @Tool(name = "get_overall_sla_performance", description = """
            Return governed overall SLA evidence for the pinned reporting snapshot.
            Use for questions about total SLA attainment, total breach rate, or overall operation counts.
            Preserve the returned metric definition and interpretation boundary; do not infer causes.
            """)
    public ToolResponse<OverallSlaEvidence> getOverallSlaPerformance() {
        AnalyticsSnapshot snapshot = snapshotRepository.get();
        int eligible = snapshot.eligibleOperations();
        int withinSla = snapshot.withinSlaOperations();
        int breached = eligible - withinSla;

        ToolResponse<OverallSlaEvidence> response = envelope(snapshot, new OverallSlaEvidence(
                "sla_attainment_rate",
                snapshot.metricDefinition(),
                snapshot.metricEligiblePopulation(),
                eligible,
                withinSla,
                breached,
                percent(withinSla, eligible),
                percent(breached, eligible)));
        invocationRecorder.record("get_overall_sla_performance", response);
        return response;
    }

    @Tool(name = "compare_service_sla_performance", description = """
            Return governed SLA evidence for every assigned team in the pinned reporting snapshot.
            Use for assigned-team comparisons, including highest breach rate or lowest attainment rate.
            Results identify the comparison dimension and are ordered by breach rate descending.
            'Worst' is ambiguous unless the user names a metric.
            Preserve the interpretation boundary and do not infer causes for observed differences.
            """)
    public ToolResponse<ServiceComparisonEvidence> compareServiceSlaPerformance() {
        AnalyticsSnapshot snapshot = snapshotRepository.get();
        List<GroupSlaEvidence> groups = snapshot.groups().stream()
                .sorted(Comparator.comparing(ComparisonGroupMetric::slaBreachRatePercent).reversed())
                .map(metric -> new GroupSlaEvidence(
                        metric.group(),
                        metric.eligibleOperations(),
                        metric.withinSlaOperations(),
                        metric.breachedOperations(),
                        metric.slaAttainmentRatePercent(),
                        metric.slaBreachRatePercent()))
                .toList();

        ToolResponse<ServiceComparisonEvidence> response = envelope(snapshot, new ServiceComparisonEvidence(
                "sla_breach_rate",
                "sla_breaches / closed_requests",
                snapshot.metricEligiblePopulation(),
                snapshot.comparisonDimension(),
                "sla_breach_rate_percent descending",
                groups));
        invocationRecorder.record("compare_service_sla_performance", response);
        return response;
    }

    private static BigDecimal percent(int numerator, int denominator) {
        return BigDecimal.valueOf(numerator)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(denominator), 2, RoundingMode.HALF_UP);
    }

    private static <T> ToolResponse<T> envelope(AnalyticsSnapshot snapshot, T evidence) {
        return new ToolResponse<>(
                snapshot.contractVersion(),
                snapshot.snapshotId(),
                snapshot.asOfDate(),
                snapshot.period(),
                snapshot.provenance(),
                evidence,
                new InterpretationBoundary(
                        snapshot.interpretationBoundary().supportedInterpretations(),
                        snapshot.interpretationBoundary().unsupportedInterpretations(),
                        snapshot.interpretationBoundary().requiredLanguage()));
    }
}
