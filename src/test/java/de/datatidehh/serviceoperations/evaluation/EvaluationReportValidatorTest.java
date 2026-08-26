package de.datatidehh.serviceoperations.evaluation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EvaluationReportValidatorTest {

    private final EvaluationReportValidator validator = new EvaluationReportValidator(new EvaluationJsonCodec());

    @Test
    void rejectsAnUnsupportedManifestSchema() {
        EvaluationRun valid = EvaluationTestFixtures.run(true, true, true, true);
        EvaluationRunManifest manifest = valid.manifest();
        EvaluationRun invalid = new EvaluationRun(
                new EvaluationRunManifest(
                        "2.0.0",
                        manifest.applicationVersion(),
                        manifest.provider(),
                        manifest.model(),
                        manifest.sourceRevision(),
                        manifest.runAt(),
                        manifest.promptVersions(),
                        manifest.resourceFingerprints()),
                valid.results());

        assertThatThrownBy(() -> validator.validate(invalid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("unsupported schema version");
    }
}
