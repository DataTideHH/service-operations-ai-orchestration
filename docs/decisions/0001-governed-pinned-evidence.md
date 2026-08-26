# ADR 0001: Governed pinned evidence and deterministic CI

## Status

Accepted for V1.

## Decision

V1 reads one versioned classpath snapshot and exposes it through exactly two read-only Spring AI tools. Every tool response carries an interpretation boundary alongside the analytical evidence.

Normal CI executes deterministic Java tests only. Real LLM runs are documented under `evidence/`, outside the required build.

## Consequences

- CI is reproducible and independent of model availability, rate limits, credentials, and behavior drift.
- Metric definitions, numerators, denominators, snapshot identity, and unsupported interpretations remain visible to the model.
- Updating evidence is deliberate rather than an implicit live-data change.
- V1 cannot answer causal questions or provide live operational data; it must say so.
