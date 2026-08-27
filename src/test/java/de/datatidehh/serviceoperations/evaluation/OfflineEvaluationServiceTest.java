package de.datatidehh.serviceoperations.evaluation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OfflineEvaluationServiceTest {

    @Autowired
    private OfflineEvaluationService evaluationService;

    @Test
    void createsClearlyLabelledPassingReferenceRunWithoutAProvider() {
        EvaluationRun run = evaluationService.createRun(Instant.parse("2026-08-27T08:00:00Z"));

        assertThat(run.manifest().provider()).isEqualTo("offline-simulation");
        assertThat(run.manifest().model()).isEqualTo("deterministic-reference-v2");
        assertThat(run.passed()).isTrue();
        assertThat(run.results()).hasSize(4).allSatisfy(result -> {
            assertThat(result.toolInvocations()).singleElement()
                    .extracting(ToolInvocation::toolName)
                    .isEqualTo(result.evaluationCase().expectedTool());
            assertThat(result.assessment().rationale()).contains("not an LLM judgment");
        });
        assertThat(run.results().get(2).observedAnswer()).contains("underspecified", "not a general");
        assertThat(run.results().get(3).observedAnswer()).contains("cannot establish why", "unsupported");
    }
}
