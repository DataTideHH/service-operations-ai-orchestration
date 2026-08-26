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

        assertThat(response.contractVersion()).isEqualTo("1.0.0");
        assertThat(response.snapshotId()).isEqualTo("fsoa-2026-q2-v1");
        assertThat(response.evidence().slaAttainmentRatePercent()).isEqualByComparingTo("92.00");
        assertThat(response.evidence().slaBreachRatePercent()).isEqualByComparingTo("8.00");
        assertBoundaryPresent(response.interpretationBoundary());
    }

    @Test
    void ordersServiceComparisonByHighestBreachRate() {
        ToolResponse<ServiceComparisonEvidence> response = tools.compareServiceSlaPerformance();

        assertThat(response.evidence().services())
                .extracting(ServiceSlaEvidence::service)
                .containsExactly("billing", "provisioning", "support");
        assertThat(response.evidence().services().getFirst().slaBreachRatePercent())
                .isEqualByComparingTo("15.20");
        assertBoundaryPresent(response.interpretationBoundary());
    }

    private static void assertBoundaryPresent(InterpretationBoundary boundary) {
        assertThat(boundary).isNotNull();
        assertThat(boundary.supportedInterpretations()).isNotEmpty();
        assertThat(boundary.unsupportedInterpretations())
                .anyMatch(value -> value.toLowerCase().contains("root causes"));
        assertThat(boundary.requiredLanguage()).contains("observational");
    }
}
