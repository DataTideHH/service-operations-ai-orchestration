package de.datatidehh.serviceoperations.snapshot;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Repository
public class ClasspathAnalyticsSnapshotRepository implements AnalyticsSnapshotRepository {

    static final String DEFAULT_LOCATION = "analytics/service-operations-snapshot-v1.properties";

    private final AnalyticsSnapshot snapshot;

    public ClasspathAnalyticsSnapshotRepository() {
        this(DEFAULT_LOCATION);
    }

    ClasspathAnalyticsSnapshotRepository(String location) {
        this.snapshot = load(location);
    }

    @Override
    public AnalyticsSnapshot get() {
        return snapshot;
    }

    private static AnalyticsSnapshot load(String location) {
        Properties properties = new Properties();
        try (InputStream input = new ClassPathResource(location).getInputStream()) {
            properties.load(input);
        }
        catch (IOException exception) {
            throw new UncheckedIOException("Could not load pinned analytics snapshot: " + location, exception);
        }

        List<ServiceMetric> services = Arrays.stream(required(properties, "services").split(","))
                .map(String::trim)
                .map(service -> new ServiceMetric(
                        service,
                        integer(properties, "service." + service + ".handled"),
                        integer(properties, "service." + service + ".within-sla")))
                .toList();

        AnalyticsSnapshot snapshot = new AnalyticsSnapshot(
                required(properties, "snapshot.id"),
                required(properties, "contract.version"),
                LocalDate.parse(required(properties, "as-of-date")),
                required(properties, "period"),
                required(properties, "metric.definition"),
                services);

        int declaredHandled = integer(properties, "overall.handled");
        int declaredWithinSla = integer(properties, "overall.within-sla");
        if (snapshot.handledOperations() != declaredHandled
                || snapshot.withinSlaOperations() != declaredWithinSla) {
            throw new IllegalStateException("Pinned overall totals do not match service-level evidence");
        }
        return snapshot;
    }

    private static int integer(Properties properties, String key) {
        return Integer.parseInt(required(properties, key));
    }

    private static String required(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required snapshot property: " + key);
        }
        return value.trim();
    }
}
