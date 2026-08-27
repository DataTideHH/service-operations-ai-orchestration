package de.datatidehh.serviceoperations.snapshot;

import com.networknt.schema.InputFormat;
import com.networknt.schema.Schema;
import com.networknt.schema.SchemaRegistry;
import com.networknt.schema.SpecificationVersion;

import java.util.List;

final class GovernedSnapshotSchemaValidator {

    void validate(String snapshotJson, String schemaJson) {
        Schema schema = SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_2020_12)
                .getSchema(schemaJson);
        schema.initializeValidators();
        List<com.networknt.schema.Error> errors = schema.validate(
                snapshotJson,
                InputFormat.JSON,
                context -> context.executionConfig(config -> config.formatAssertionsEnabled(true)));
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Pinned analytics snapshot violates its JSON Schema: " + errors);
        }
    }
}
