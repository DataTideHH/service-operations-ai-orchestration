# ADR 0003: Evaluation artifacts are versioned, fingerprinted, and comparable

## Status

Accepted for V3.

## Decision

V3 externalizes the answer, judge-system, and judge-case prompts as versioned classpath resources. Every model-dependent run records explicit prompt versions and SHA-256 fingerprints for the pinned analytics snapshot, evaluation catalog, and all three prompts.

The runner writes one Markdown report for review and one JSON report for deterministic processing. Both are projections of the same `EvaluationRun`. The JSON artifact is read back and validated immediately after writing; an invalid artifact fails the run.

Offline commands validate a JSON report or compare a candidate report with a baseline. Comparison is permitted only when all governed resource fingerprints match. A passing baseline case that fails in the candidate is a regression and makes the comparison fail.

## Consequences

- A report identifies the exact governed inputs used for its observations.
- Prompt changes become explicit, reviewable source changes.
- Markdown remains convenient for human review while JSON supports deterministic checks.
- Reports created from different governed evidence or prompts are not presented as directly comparable.
- Validation and comparison do not call an LLM and require no provider credentials.
- V3 does not change the two-tool surface, analytics contract, or interpretation boundary.
