# LLM evaluation evidence

This directory separates real, model-dependent observations from deterministic offline reference evidence. Neither is a normal CI gate.

Live-provider runs generate `runs/evaluation-report.md` and `runs/evaluation-report.json` from the same run. The manifest records provider, model, source revision, prompt versions, and governed resource fingerprints. Do not silently rewrite a model answer or assessment to make it pass. Generated reports are workflow artifacts by default; commit one only after deliberate review.

The JSON artifact is the machine-readable source for deterministic validation and comparison. Reports are comparable only when their snapshot, evaluation-case, and prompt fingerprints match. The comparison command writes `runs/evaluation-comparison.md` and fails on incompatible inputs or pass-to-fail regressions.

The four governed cases cover:

1. overall SLA semantics;
2. highest observed breach rate;
3. ambiguity of an unqualified “worst” ranking;
4. the causal interpretation boundary.

## Offline reference

`offline-reference-v3/` preserves the historical V3 simulation. `offline-reference-v4/` contains the current simulation against the imported analytics handoff. Both are produced without provider credentials. Their answers are authored references, not model observations, and their deterministic assessments are not evidence of model quality.

The two baselines are intentionally incompatible: V4 changes the governed snapshot, adds the producer schema to the manifest fingerprint set and updates the evaluation catalog to the reconciled assigned-team evidence.
