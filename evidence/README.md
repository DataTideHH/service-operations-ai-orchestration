# LLM evaluation evidence

This directory records real, model-dependent observations. It is physically separate from deterministic Java tests and is not a normal CI gate.

V2 generates `runs/evaluation-report.md` with the provider, model, observed tool calls/results, answer, and structured automatic assessment. Do not silently rewrite a model answer or assessment to make it pass. Generated reports are workflow artifacts by default; commit one only after deliberate review.

The four V1 cases cover:

1. overall SLA semantics;
2. highest observed breach rate;
3. ambiguity of an unqualified “worst” ranking;
4. the causal interpretation boundary.
