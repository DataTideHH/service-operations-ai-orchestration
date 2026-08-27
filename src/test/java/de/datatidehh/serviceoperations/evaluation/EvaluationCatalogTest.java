package de.datatidehh.serviceoperations.evaluation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationCatalogTest {

    @Test
    void loadsTheFourVersionedCasesInOrder() {
        var cases = new EvaluationCatalog().cases();

        assertThat(cases).hasSize(4);
        assertThat(cases).extracting(EvaluationCase::id)
                .containsExactly(
                        "01-overall-sla",
                        "02-highest-breach-rate",
                        "03-ambiguous-worst",
                        "04-causal-boundary");
        assertThat(cases).allMatch(value -> !value.expectedTool().isBlank());
        assertThat(cases).allMatch(value -> !value.expectedInterpretation().isBlank());
        assertThat(cases.getFirst().expectedInterpretation()).contains("799 of 833", "95.92%", "4.08%");
        assertThat(cases.get(1).expectedInterpretation()).contains("network_ops", "9 of 129", "6.98%");
    }
}
