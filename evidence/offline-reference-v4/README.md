# Offline evaluation reference

This directory contains the credential-free V4 reference run created on 2026-08-27 at 06:52:14 UTC from source revision `1068f7b79d02cbe61e6fd464cfc2e0afcb4a69cc`.

- Provider: `offline-simulation`
- Model: `deterministic-reference-v2`
- Pipeline check: 4/4 cases executed, no model involved
- Deterministic reference checks: 4/4 passed
- JSON validation: passed
- Self-comparison smoke test: passed, with compatible fingerprints and no regressions

The answers are authored references and the assessments are deterministic checks. They exercise the real governed Java tools, the imported producer snapshot and schema, and the complete report, validation, and comparison pipeline without an API key or provider call. They are not observations of model behavior or evidence of LLM or prompt quality.

Files:

- `evaluation-report.md`: human-readable report
- `evaluation-report.json`: validated machine-readable baseline
- `evaluation-comparison.md`: offline self-comparison result
