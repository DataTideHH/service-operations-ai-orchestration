package de.datatidehh.serviceoperations.tool;

import de.datatidehh.serviceoperations.snapshot.AnalyticsSnapshot;
import de.datatidehh.serviceoperations.snapshot.AnalyticsSnapshotRepository;
import de.datatidehh.serviceoperations.snapshot.ServiceMetric;
import de.datatidehh.serviceoperations.evaluation.ToolInvocationRecorder;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@Component
public class ServiceOperationsAnalyticsTools {

    private static final InterpretationBoundary BOUNDARY = InterpretationBoundary.governedSlaBoundary();

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
        int handled = snapshot.handledOperations();
        int withinSla = snapshot.withinSlaOperations();
        int breached = handled - withinSla;

        ToolResponse<OverallSlaEvidence> response = envelope(snapshot, new OverallSlaEvidence(
                "sla_attainment_rate",
                snapshot.metricDefinition(),
                handled,
                withinSla,
                breached,
                percent(withinSla, handled),
                percent(breached, handled)));
        invocationRecorder.record("get_overall_sla_performance", response);
        return response;
    }

    @Tool(name = "compare_service_sla_performance", description = """
            Return governed SLA evidence for every service in the pinned reporting snapshot.
            Use for comparisons, including highest breach rate or lowest attainment rate.
            Results are ordered by breach rate descending. 'Worst' is ambiguous unless the user names a metric.
            Preserve the interpretation boundary and do not infer causes for observed differences.
            """)
    public ToolResponse<ServiceComparisonEvidence> compareServiceSlaPerformance() {
        AnalyticsSnapshot snapshot = snapshotRepository.get();
        List<ServiceSlaEvidence> services = snapshot.services().stream()
                .sorted(Comparator.comparing(ServiceMetric::slaBreachRatePercent).reversed())
                .map(metric -> new ServiceSlaEvidence(
                        metric.service(),
                        metric.handledOperations(),
                        metric.withinSlaOperations(),
                        metric.breachedOperations(),
                        metric.slaAttainmentRatePercent(),
                        metric.slaBreachRatePercent()))
                .toList();

        ToolResponse<ServiceComparisonEvidence> response = envelope(snapshot, new ServiceComparisonEvidence(
                "sla_breach_rate",
                "breached_operations / handled_operations",
                "sla_breach_rate_percent descending",
                services));
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
                evidence,
                BOUNDARY);
    }
}
