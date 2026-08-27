package de.datatidehh.serviceoperations.tool;

import de.datatidehh.serviceoperations.snapshot.ClasspathAnalyticsSnapshotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.ai.tool.annotation.Tool;
import de.datatidehh.serviceoperations.evaluation.ToolInvocationRecorder;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceOperationsAnalyticsToolsTest {

    private final ServiceOperationsAnalyticsTools tools =
            new ServiceOperationsAnalyticsTools(
                    new ClasspathAnalyticsSnapshotRepository(),
                    new ToolInvocationRecorder());

    @Test
    void exposesExactlyTwoReadOnlyNoArgumentTools() {
        List<Method> toolMethods = Arrays.stream(ServiceOperationsAnalyticsTools.class.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Tool.class))
                .toList();

        assertThat(toolMethods).hasSize(2);
        assertThat(toolMethods).allMatch(method -> method.getParameterCount() == 0);
        assertThat(toolMethods).extracting(method -> method.getAnnotation(Tool.class).name())
                .containsExactlyInAnyOrder(
                        "get_overall_sla_performance",
                        "compare_service_sla_performance");
    }

    @Test
    void returnsGovernedOverallEvidence() {
        ToolResponse<OverallSlaEvidence> response = tools.getOverallSlaPerformance();

        assertThat(response.contractVersion()).isEqualTo("2.0.0");
        assertThat(response.snapshotId()).isEqualTo("fsoa-2026-01-01-2026-03-31-v1");
        assertThat(response.evidence().eligibleOperations()).isEqualTo(833);
        assertThat(response.evidence().slaAttainmentRatePercent()).isEqualByComparingTo("95.92");
        assertThat(response.evidence().slaBreachRatePercent()).isEqualByComparingTo("4.08");
        assertThat(response.provenance().producerApplication())
                .isEqualTo("fabric-service-operations-analytics");
        assertBoundaryPresent(response.interpretationBoundary());
    }

    @Test
    void ordersServiceComparisonByHighestBreachRate() {
        ToolResponse<ServiceComparisonEvidence> response = tools.compareServiceSlaPerformance();

        assertThat(response.evidence().comparisonDimension()).isEqualTo("assigned_team");
        assertThat(response.evidence().groups())
                .extracting(GroupSlaEvidence::group)
                .containsExactly("network_ops", "data_platform", "business_apps", "service_desk", "workplace");
        assertThat(response.evidence().groups().getFirst().slaBreachRatePercent())
                .isEqualByComparingTo("6.98");
        assertBoundaryPresent(response.interpretationBoundary());
    }

    private static void assertBoundaryPresent(InterpretationBoundary boundary) {
        assertThat(boundary).isNotNull();
        assertThat(boundary.supportedInterpretations()).isNotEmpty();
        assertThat(boundary.unsupportedInterpretations())
                .anyMatch(value -> value.toLowerCase().contains("root cause"));
        assertThat(boundary.requiredLanguage()).contains("observational");
    }
}
