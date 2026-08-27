# ADR 0004: Offline reference evidence is separate from model evidence

## Status

Accepted after V3.

## Decision

The application provides an `artifact-command=simulate` mode that executes all four evaluation cases through the governed Java tools and writes the same Markdown and JSON artifact formats as a provider-backed run. Its manifest uses provider `offline-simulation` and model `deterministic-reference-v1`.

The answers and assessments in this mode are curated deterministic references. They validate tool execution, report generation, JSON round-trip validation, and offline comparison, but they do not measure model behavior or prompt quality. Committed simulations live under `evidence/offline-reference/`; real provider observations remain under `evidence/runs/` or in workflow artifacts.

## Consequences

- The complete V3 artifact pipeline can be demonstrated without credentials or API cost.
- Simulation evidence cannot be mistaken for a live LLM evaluation.
- A live provider run is still required before making claims about a model.
- V4 work can start independently of provider billing availability.
