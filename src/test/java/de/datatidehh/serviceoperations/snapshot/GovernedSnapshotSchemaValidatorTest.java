package de.datatidehh.serviceoperations.snapshot;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GovernedSnapshotSchemaValidatorTest {

    @Test
    void rejectsAnIncompleteSnapshotBeforeDeserialization() throws IOException {
        String schema = new ClassPathResource(ClasspathAnalyticsSnapshotRepository.DEFAULT_SCHEMA_LOCATION)
                .getContentAsString(StandardCharsets.UTF_8);

        assertThatThrownBy(() -> new GovernedSnapshotSchemaValidator()
                .validate("{\"schemaVersion\":\"1.0.0\"}", schema))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("violates its JSON Schema");
    }
}
