package de.datatidehh.serviceoperations.snapshot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class ClasspathAnalyticsSnapshotRepository implements AnalyticsSnapshotRepository {

    public static final String DEFAULT_LOCATION = "analytics/ai-service-operations-snapshot-v1.json";
    public static final String DEFAULT_SCHEMA_LOCATION = "contracts/ai-service-operations-snapshot-v1.schema.json";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final AnalyticsSnapshot snapshot;

    public ClasspathAnalyticsSnapshotRepository() {
        this(DEFAULT_LOCATION, DEFAULT_SCHEMA_LOCATION);
    }

    ClasspathAnalyticsSnapshotRepository(String location, String schemaLocation) {
        this.snapshot = load(location, schemaLocation);
    }

    @Override
    public AnalyticsSnapshot get() {
        return snapshot;
    }

    private static AnalyticsSnapshot load(String location, String schemaLocation) {
        String snapshotJson = resourceText(location);
        String schemaJson = resourceText(schemaLocation);
        new GovernedSnapshotSchemaValidator().validate(snapshotJson, schemaJson);

        try {
            JsonNode root = OBJECT_MAPPER.readTree(snapshotJson);
            JsonNode source = root.required("source");
            JsonNode producer = root.required("producer");
            JsonNode metric = root.required("metric");
            JsonNode period = root.required("reportingPeriod");
            JsonNode boundary = root.required("interpretationBoundary");

            List<ComparisonGroupMetric> groups = new ArrayList<>();
            for (JsonNode group : root.required("groups")) {
                ComparisonGroupMetric parsed = new ComparisonGroupMetric(
                        group.required("group").textValue(),
                        group.required("eligibleOperations").intValue(),
                        group.required("withinSlaOperations").intValue());
                requireEvidenceMatches(group, parsed, "group " + parsed.group());
                groups.add(parsed);
            }

            AnalyticsSnapshot snapshot = new AnalyticsSnapshot(
                    root.required("schemaVersion").textValue(),
                    root.required("snapshotId").textValue(),
                    root.required("contractVersion").textValue(),
                    LocalDate.parse(root.required("asOfDate").textValue()),
                    period.required("label").textValue(),
                    metric.required("definition").textValue(),
                    metric.required("eligiblePopulation").textValue(),
                    root.required("comparisonDimension").textValue(),
                    new SnapshotProvenance(
                            producer.required("application").textValue(),
                            producer.required("version").textValue(),
                            source.required("repository").textValue(),
                            source.required("revision").textValue(),
                            source.required("scenario").textValue(),
                            source.required("ingestionBatchId").textValue(),
                            stringMap(source.required("resourceFingerprints"))),
                    new SnapshotBoundary(
                            stringList(boundary.required("supportedInterpretations")),
                            stringList(boundary.required("unsupportedInterpretations")),
                            boundary.required("requiredLanguage").textValue()),
                    groups);
            validateReconciliation(snapshot, root.required("overall"));
            return snapshot;
        }
        catch (IOException exception) {
            throw new UncheckedIOException("Could not parse pinned analytics snapshot: " + location, exception);
        }
    }

    private static void validateReconciliation(AnalyticsSnapshot snapshot, JsonNode overall) {
        int eligible = overall.required("eligibleOperations").intValue();
        int withinSla = overall.required("withinSlaOperations").intValue();
        int breached = overall.required("breachedOperations").intValue();
        if (snapshot.eligibleOperations() != eligible
                || snapshot.withinSlaOperations() != withinSla
                || withinSla + breached != eligible) {
            throw new IllegalStateException("Pinned overall totals do not match comparison-group evidence");
        }
        requireRate(overall, "slaAttainmentRatePercent", withinSla, eligible, "overall attainment");
        requireRate(overall, "slaBreachRatePercent", breached, eligible, "overall breach");

        Set<String> groups = new HashSet<>();
        BigDecimal priorRate = null;
        for (ComparisonGroupMetric group : snapshot.groups()) {
            if (!groups.add(group.group())) {
                throw new IllegalStateException("Duplicate comparison group: " + group.group());
            }
            BigDecimal rate = group.slaBreachRatePercent();
            if (priorRate != null && priorRate.compareTo(rate) < 0) {
                throw new IllegalStateException("Comparison groups are not ordered by breach rate descending");
            }
            priorRate = rate;
        }
    }

    private static void requireEvidenceMatches(JsonNode node, ComparisonGroupMetric metric, String label) {
        int breached = node.required("breachedOperations").intValue();
        if (breached != metric.breachedOperations()) {
            throw new IllegalStateException("SLA counts do not reconcile for " + label);
        }
        requireRate(node, "slaAttainmentRatePercent", metric.withinSlaOperations(),
                metric.eligibleOperations(), label + " attainment");
        requireRate(node, "slaBreachRatePercent", breached,
                metric.eligibleOperations(), label + " breach");
    }

    private static void requireRate(JsonNode node, String field, int numerator, int denominator, String label) {
        BigDecimal expected = BigDecimal.valueOf(numerator)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(denominator), 2, RoundingMode.HALF_UP);
        BigDecimal actual = node.required(field).decimalValue();
        if (expected.compareTo(actual) != 0) {
            throw new IllegalStateException("Derived rate does not match declared rate for " + label);
        }
    }

    private static List<String> stringList(JsonNode array) {
        List<String> values = new ArrayList<>();
        array.forEach(value -> values.add(value.textValue()));
        return values;
    }

    private static Map<String, String> stringMap(JsonNode object) {
        Map<String, String> values = new LinkedHashMap<>();
        Iterator<Map.Entry<String, JsonNode>> fields = object.properties().iterator();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            values.put(field.getKey(), field.getValue().textValue());
        }
        return values;
    }

    private static String resourceText(String location) {
        try {
            return new ClassPathResource(location).getContentAsString(StandardCharsets.UTF_8);
        }
        catch (IOException exception) {
            throw new UncheckedIOException("Could not load pinned analytics resource: " + location, exception);
        }
    }
}
