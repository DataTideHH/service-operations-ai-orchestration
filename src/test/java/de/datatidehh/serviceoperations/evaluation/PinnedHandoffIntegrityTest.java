package de.datatidehh.serviceoperations.evaluation;

import de.datatidehh.serviceoperations.snapshot.ClasspathAnalyticsSnapshotRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PinnedHandoffIntegrityTest {

    @Test
    void importedProducerArtifactsHaveTheReviewedFingerprints() {
        ResourceFingerprintService fingerprints = new ResourceFingerprintService();

        assertThat(fingerprints.sha256(ClasspathAnalyticsSnapshotRepository.DEFAULT_LOCATION))
                .isEqualTo("81c32ce8afd6b74c06476ee628ab405ad0c7f9b9e2369b08d62ddb04bf6ef854");
        assertThat(fingerprints.sha256(ClasspathAnalyticsSnapshotRepository.DEFAULT_SCHEMA_LOCATION))
                .isEqualTo("9a4729fab1a22838fcf7a753a271848a42488e58d1cf5e5228e0aae027816c06");
    }
}
