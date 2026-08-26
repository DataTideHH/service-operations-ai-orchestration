# LLM evaluation evidence

This directory records real, model-dependent observations. It is physically separate from deterministic Java tests and is not a normal CI gate.

V3 generates `runs/evaluation-report.md` and `runs/evaluation-report.json` from the same run. The manifest records provider, model, source revision, prompt versions, and governed resource fingerprints. Do not silently rewrite a model answer or assessment to make it pass. Generated reports are workflow artifacts by default; commit one only after deliberate review.

The JSON artifact is the machine-readable source for deterministic validation and comparison. Reports are comparable only when their snapshot, evaluation-case, and prompt fingerprints match. The comparison command writes `runs/evaluation-comparison.md` and fails on incompatible inputs or pass-to-fail regressions.

The four V1 cases cover:

1. overall SLA semantics;
2. highest observed breach rate;
3. ambiguity of an unqualified “worst” ranking;
4. the causal interpretation boundary.
