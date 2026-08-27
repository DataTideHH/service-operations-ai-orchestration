package de.datatidehh.serviceoperations.evaluation;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationPromptCatalogTest {

    @Test
    void loadsVersionedPromptsAndRendersTheUntrustedAnswerBoundary() {
        EvaluationPromptCatalog prompts = new EvaluationPromptCatalog();
        EvaluationCase evaluationCase = new EvaluationCase(
                "case", "Question?", "expected_tool", "Retain semantics.");

        String rendered = prompts.renderJudgeCase(
                evaluationCase,
                List.of(new ToolInvocation("expected_tool", "result")),
                "Observed answer.");

        assertThat(prompts.answerSystem()).contains("governed service-operations evidence");
        assertThat(prompts.judgeSystem()).contains("untrusted evidence");
        assertThat(rendered).contains("<observed-answer>", "Observed answer.", "expected_tool");
    }

    @Test
    void fingerprintsEveryGovernedResource() {
        var fingerprints = new ResourceFingerprintService()
                .sha256(EvaluationRunManifestFactory.GOVERNED_RESOURCE_PATHS);

        assertThat(fingerprints).hasSize(6);
        assertThat(fingerprints.values()).allMatch(value -> value.matches("[0-9a-f]{64}"));
    }
}
