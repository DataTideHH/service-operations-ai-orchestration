package de.datatidehh.serviceoperations.evaluation;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationRunComparatorTest {

    private final EvaluationRunComparator comparator = new EvaluationRunComparator();

    @Test
    void detectsARegressionBetweenCompatibleRuns() {
        EvaluationComparison comparison = comparator.compare(
                EvaluationTestFixtures.run(true, true, false, true),
                EvaluationTestFixtures.run(true, false, true, true));

        assertThat(comparison.compatible()).isTrue();
        assertThat(comparison.regressions()).containsExactly("case-2");
        assertThat(comparison.improvements()).containsExactly("case-3");
        assertThat(comparison.passed()).isFalse();
    }

    @Test
    void rejectsComparisonWhenGovernedEvidenceChanged() {
        EvaluationRun baseline = EvaluationTestFixtures.run(true, true, true, true);
        var changedFingerprints = new LinkedHashMap<>(EvaluationTestFixtures.fingerprints());
        changedFingerprints.put(EvaluationPromptCatalog.JUDGE_CASE_PATH, "b".repeat(64));
        EvaluationRun candidate = new EvaluationRun(
                EvaluationTestFixtures.manifest(changedFingerprints),
                baseline.results());

        EvaluationComparison comparison = comparator.compare(baseline, candidate);

        assertThat(comparison.compatible()).isFalse();
        assertThat(comparison.incompatibilities()).containsExactly("governed resource fingerprints differ");
    }
}
