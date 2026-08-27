# Changelog

This file summarizes the implemented project increments. It is not a release schedule or a roadmap.

## V4 — governed analytics handoff

- replaced the standalone properties fixture with the exact producer JSON artifact and Draft 2020-12 schema
- added upstream provenance, source fingerprints, startup validation, and independent reconciliation
- advanced the external tool contract to `2.0.0` with explicit `assigned_team` comparison semantics
- updated the four cases and recorded a separate credential-free V4 pipeline baseline

## V3 — reproducible evaluation artifacts

- versioned answer and judge prompts
- added run manifests, governed fingerprints, and machine-readable JSON reports
- added deterministic report validation and regression comparison

## V2 — executable model evaluation

- added provider-configurable OpenAI and Anthropic evaluation runners
- captured tool calls and structured assessments
- added Markdown reports and a manual credential-dependent GitHub workflow

No live-provider result has been committed.

## V1 — governed tool surface

- introduced exactly two read-only Spring AI tools over pinned evidence
- made metric definitions, denominators, snapshot identity, and interpretation boundaries explicit
- kept normal tests and CI deterministic and independent of provider credentials
