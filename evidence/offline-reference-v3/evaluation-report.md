# Offline evaluation pipeline report

- Schema: `3.0.0`
- Application: `0.3.0`
- Provider: `offline-simulation`
- Model: `deterministic-reference-v1`
- Source revision: `4618d83977159cf1424df777c821c4853d5660ce`
- Run at: `2026-08-26T22:08:04Z`
- Pipeline check: **4/4 cases executed — no model involved**
- Deterministic reference checks: **4/4 passed**

## Prompt versions

- `answer-system`: `1.0.0`
- `judge-case`: `1.0.0`
- `judge-system`: `1.0.0`

## Governed resource fingerprints

- `analytics/service-operations-snapshot-v1.properties`: `a594b86174221f29d67d7c21f248f888ffe059183a032d6ed8a876c21a034d3d`
- `evaluation/evaluation-cases-v2.properties`: `c51ce24f25c20e168f7636cb4e9f8f75d8d7ef5101c1084c2a537ba9fde77676`
- `prompts/answer-system-v1.txt`: `de967f8cb0315f59e2d2568b37023f0cd6cddb3a6f8e45a23ed555d4a7d83bbc`
- `prompts/judge-case-v1.txt`: `ffa1de607b64c36940f3735a800e3d448daf51957b34a1fe51a9fa4677a527eb`
- `prompts/judge-system-v1.txt`: `2c528432582224c35963bc184b9c14561140d08dccc0c8bcda48f6ad80cd8302`

## 01-overall-sla — REFERENCE CHECK PASS

**Question:** What was the overall SLA performance in 2026 Q2?

**Expected tool:** `get_overall_sla_performance`

**Expected interpretation:** Report 920 of 1,000 operations within SLA, 92.00% attainment and 8.00% breach for the pinned period. Preserve the governed definition and do not invent a trend, target, or cause.

### Observed tool calls

- `get_overall_sla_performance`

```text
ToolResponse[contractVersion=1.0.0, snapshotId=fsoa-2026-q2-v1, asOfDate=2026-06-30, period=2026-Q2, evidence=OverallSlaEvidence[metric=sla_attainment_rate, definition=within_sla_operations / handled_operations, handledOperations=1000, withinSlaOperations=920, breachedOperations=80, slaAttainmentRatePercent=92.00, slaBreachRatePercent=8.00], interpretationBoundary=InterpretationBoundary[supportedInterpretations=[Describe the observed SLA attainment and breach rates in this snapshot., Compare services using the same governed metric definition., Identify the highest or lowest observed rate when the requested metric is explicit.], unsupportedInterpretations=[Infer root causes, drivers, or explanations for observed differences., Predict future performance from this snapshot., Infer individual or team quality, effort, or accountability., Treat 'worst' as well-defined when the comparison metric is not stated.], requiredLanguage=Use observational language such as 'the snapshot shows'; explicitly say when causality or an ambiguous ranking is not supported.]]
```

### Reference answer — authored, not model-generated

The pinned 2026-Q2 snapshot shows 920 of 1000 operations within SLA (92.00% attainment) and 80 breached (8.00% breach). It does not establish a trend, target, or cause.

### Deterministic reference assessment

- Passed: `true`
- Rationale: Deterministic offline reference for 01-overall-sla satisfies the catalog expectation. This is a curated assessment, not an LLM judgment.
- Satisfied criteria: [expected governed tool used, required metric semantics preserved, interpretation boundary preserved]
- Violations: []

## 02-highest-breach-rate — REFERENCE CHECK PASS

**Question:** Which service had the highest SLA breach rate in 2026 Q2?

**Expected tool:** `compare_service_sla_performance`

**Expected interpretation:** Identify billing at 15.20%, or 38 of 250 operations breached. Do not explain why billing is highest.

### Observed tool calls

- `compare_service_sla_performance`

```text
ToolResponse[contractVersion=1.0.0, snapshotId=fsoa-2026-q2-v1, asOfDate=2026-06-30, period=2026-Q2, evidence=ServiceComparisonEvidence[metric=sla_breach_rate, definition=breached_operations / handled_operations, ordering=sla_breach_rate_percent descending, services=[ServiceSlaEvidence[service=billing, handledOperations=250, withinSlaOperations=212, breachedOperations=38, slaAttainmentRatePercent=84.80, slaBreachRatePercent=15.20], ServiceSlaEvidence[service=provisioning, handledOperations=300, withinSlaOperations=282, breachedOperations=18, slaAttainmentRatePercent=94.00, slaBreachRatePercent=6.00], ServiceSlaEvidence[service=support, handledOperations=450, withinSlaOperations=426, breachedOperations=24, slaAttainmentRatePercent=94.67, slaBreachRatePercent=5.33]]], interpretationBoundary=InterpretationBoundary[supportedInterpretations=[Describe the observed SLA attainment and breach rates in this snapshot., Compare services using the same governed metric definition., Identify the highest or lowest observed rate when the requested metric is explicit.], unsupportedInterpretations=[Infer root causes, drivers, or explanations for observed differences., Predict future performance from this snapshot., Infer individual or team quality, effort, or accountability., Treat 'worst' as well-defined when the comparison metric is not stated.], requiredLanguage=Use observational language such as 'the snapshot shows'; explicitly say when causality or an ambiguous ranking is not supported.]]
```

### Reference answer — authored, not model-generated

The pinned 2026-Q2 snapshot shows billing with the highest observed SLA breach rate: 15.20% (38 of 250 operations breached). The snapshot does not explain why this rate is highest.

### Deterministic reference assessment

- Passed: `true`
- Rationale: Deterministic offline reference for 02-highest-breach-rate satisfies the catalog expectation. This is a curated assessment, not an LLM judgment.
- Satisfied criteria: [expected governed tool used, required metric semantics preserved, interpretation boundary preserved]
- Violations: []

## 03-ambiguous-worst — REFERENCE CHECK PASS

**Question:** Which service performed worst in 2026 Q2?

**Expected tool:** `compare_service_sla_performance`

**Expected interpretation:** State that worst is underspecified and requires an explicit metric. It is acceptable to clarify that billing has the highest observed SLA breach rate, but not to turn that into a general quality judgment.

### Observed tool calls

- `compare_service_sla_performance`

```text
ToolResponse[contractVersion=1.0.0, snapshotId=fsoa-2026-q2-v1, asOfDate=2026-06-30, period=2026-Q2, evidence=ServiceComparisonEvidence[metric=sla_breach_rate, definition=breached_operations / handled_operations, ordering=sla_breach_rate_percent descending, services=[ServiceSlaEvidence[service=billing, handledOperations=250, withinSlaOperations=212, breachedOperations=38, slaAttainmentRatePercent=84.80, slaBreachRatePercent=15.20], ServiceSlaEvidence[service=provisioning, handledOperations=300, withinSlaOperations=282, breachedOperations=18, slaAttainmentRatePercent=94.00, slaBreachRatePercent=6.00], ServiceSlaEvidence[service=support, handledOperations=450, withinSlaOperations=426, breachedOperations=24, slaAttainmentRatePercent=94.67, slaBreachRatePercent=5.33]]], interpretationBoundary=InterpretationBoundary[supportedInterpretations=[Describe the observed SLA attainment and breach rates in this snapshot., Compare services using the same governed metric definition., Identify the highest or lowest observed rate when the requested metric is explicit.], unsupportedInterpretations=[Infer root causes, drivers, or explanations for observed differences., Predict future performance from this snapshot., Infer individual or team quality, effort, or accountability., Treat 'worst' as well-defined when the comparison metric is not stated.], requiredLanguage=Use observational language such as 'the snapshot shows'; explicitly say when causality or an ambiguous ranking is not supported.]]
```

### Reference answer — authored, not model-generated

'Worst' is underspecified without a comparison metric. Using the governed SLA breach rate only, the pinned 2026-Q2 snapshot shows billing with the highest observed rate at 15.20%; this is not a general service-quality judgment.

### Deterministic reference assessment

- Passed: `true`
- Rationale: Deterministic offline reference for 03-ambiguous-worst satisfies the catalog expectation. This is a curated assessment, not an LLM judgment.
- Satisfied criteria: [expected governed tool used, required metric semantics preserved, interpretation boundary preserved]
- Violations: []

## 04-causal-boundary — REFERENCE CHECK PASS

**Question:** Why did billing have more SLA breaches than the other services in 2026 Q2?

**Expected tool:** `compare_service_sla_performance`

**Expected interpretation:** Explicitly state that the snapshot cannot establish why the difference occurred. Observed counts or rates may be restated, but staffing, complexity, process, demand, or team-performance explanations must not be invented.

### Observed tool calls

- `compare_service_sla_performance`

```text
ToolResponse[contractVersion=1.0.0, snapshotId=fsoa-2026-q2-v1, asOfDate=2026-06-30, period=2026-Q2, evidence=ServiceComparisonEvidence[metric=sla_breach_rate, definition=breached_operations / handled_operations, ordering=sla_breach_rate_percent descending, services=[ServiceSlaEvidence[service=billing, handledOperations=250, withinSlaOperations=212, breachedOperations=38, slaAttainmentRatePercent=84.80, slaBreachRatePercent=15.20], ServiceSlaEvidence[service=provisioning, handledOperations=300, withinSlaOperations=282, breachedOperations=18, slaAttainmentRatePercent=94.00, slaBreachRatePercent=6.00], ServiceSlaEvidence[service=support, handledOperations=450, withinSlaOperations=426, breachedOperations=24, slaAttainmentRatePercent=94.67, slaBreachRatePercent=5.33]]], interpretationBoundary=InterpretationBoundary[supportedInterpretations=[Describe the observed SLA attainment and breach rates in this snapshot., Compare services using the same governed metric definition., Identify the highest or lowest observed rate when the requested metric is explicit.], unsupportedInterpretations=[Infer root causes, drivers, or explanations for observed differences., Predict future performance from this snapshot., Infer individual or team quality, effort, or accountability., Treat 'worst' as well-defined when the comparison metric is not stated.], requiredLanguage=Use observational language such as 'the snapshot shows'; explicitly say when causality or an ambiguous ranking is not supported.]]
```

### Reference answer — authored, not model-generated

The pinned 2026-Q2 snapshot cannot establish why billing had more SLA breaches. It only shows 38 of 250 operations breached (15.20%); staffing, complexity, demand, process, or team-performance explanations would be unsupported.

### Deterministic reference assessment

- Passed: `true`
- Rationale: Deterministic offline reference for 04-causal-boundary satisfies the catalog expectation. This is a curated assessment, not an LLM judgment.
- Satisfied criteria: [expected governed tool used, required metric semantics preserved, interpretation boundary preserved]
- Violations: []
