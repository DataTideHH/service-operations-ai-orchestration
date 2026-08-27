# Offline evaluation pipeline report

- Schema: `3.0.0`
- Application: `0.4.0`
- Provider: `offline-simulation`
- Model: `deterministic-reference-v2`
- Source revision: `1068f7b79d02cbe61e6fd464cfc2e0afcb4a69cc`
- Run at: `2026-08-27T06:52:14Z`
- Pipeline check: **4/4 cases executed — no model involved**
- Deterministic reference checks: **4/4 passed**

## Prompt versions

- `answer-system`: `1.0.0`
- `judge-case`: `1.0.0`
- `judge-system`: `1.0.0`

## Governed resource fingerprints

- `analytics/ai-service-operations-snapshot-v1.json`: `81c32ce8afd6b74c06476ee628ab405ad0c7f9b9e2369b08d62ddb04bf6ef854`
- `contracts/ai-service-operations-snapshot-v1.schema.json`: `9a4729fab1a22838fcf7a753a271848a42488e58d1cf5e5228e0aae027816c06`
- `evaluation/evaluation-cases-v4.properties`: `ca5f8852cd28f76ab9db7c19bf2302efbccbed79ef6f547481d32d04ef7d78dd`
- `prompts/answer-system-v1.txt`: `de967f8cb0315f59e2d2568b37023f0cd6cddb3a6f8e45a23ed555d4a7d83bbc`
- `prompts/judge-case-v1.txt`: `ffa1de607b64c36940f3735a800e3d448daf51957b34a1fe51a9fa4677a527eb`
- `prompts/judge-system-v1.txt`: `2c528432582224c35963bc184b9c14561140d08dccc0c8bcda48f6ad80cd8302`

## 01-overall-sla — REFERENCE CHECK PASS

**Question:** What was the overall SLA performance from 2026-01-01 through 2026-03-31?

**Expected tool:** `get_overall_sla_performance`

**Expected interpretation:** Report 799 of 833 SLA-eligible closed requests within SLA, 95.92% attainment and 4.08% breach for the pinned period. Preserve the closed-request denominator and do not invent a trend, target, or cause.

### Observed tool calls

- `get_overall_sla_performance`

```text
ToolResponse[contractVersion=2.0.0, snapshotId=fsoa-2026-01-01-2026-03-31-v1, asOfDate=2026-03-31, period=2026-01-01/2026-03-31, provenance=SnapshotProvenance[producerApplication=fabric-service-operations-analytics, producerVersion=0.4.0, sourceRepository=https://github.com/DataTideHH/fabric-service-operations-analytics, sourceRevision=0a6c4ebffe366d7133215634d836a5d9b102e7fb, scenario=deterministic-synthetic-service-operations, ingestionBatchId=292ed8fb2857e392, resourceFingerprints={evidence/sla_by_team.csv=aad6680a0f65c6c1b90daf2736da46eb8bcf222f00514b94d6dcf0af10211f4e, analytics/metric_contract.json=d8196ed1d6feebbf9f033fe6f9f3bfd297f56445a3f6b6716e67c6c446ac6fac, evidence/analytics_manifest.json=7ec3aeab96962ec84d715eb32984993f7c579cf99eacbca07216562f2af8a59b}], evidence=OverallSlaEvidence[metric=sla_attainment_rate, definition=sla_met_requests / closed_requests, eligiblePopulation=accepted requests with status = closed, eligibleOperations=833, withinSlaOperations=799, breachedOperations=34, slaAttainmentRatePercent=95.92, slaBreachRatePercent=4.08], interpretationBoundary=InterpretationBoundary[supportedInterpretations=[Describe observed SLA attainment and breach rates in this snapshot., Compare resolver teams using the same governed metric definition., Identify the highest or lowest observed rate when the metric is explicit.], unsupportedInterpretations=[The analytics layer identifies concentration and association patterns. It does not establish root cause because the source model contains no cause codes, waiting-state history, reassignment events or supplier dependencies., Predict future performance from this snapshot., Infer individual or team quality, effort, or accountability., Treat 'worst' as well-defined when the comparison metric is not stated.], requiredLanguage=Use observational language such as 'the snapshot shows'; explicitly say when causality or an ambiguous ranking is not supported.]]
```

### Reference answer — authored, not model-generated

The pinned 2026-01-01/2026-03-31 snapshot shows 799 of 833 operations within SLA (95.92% attainment) and 34 breached (4.08% breach) among SLA-eligible closed requests. It does not establish a trend, target, or cause.

### Deterministic reference assessment

- Passed: `true`
- Rationale: Deterministic offline reference for 01-overall-sla satisfies the catalog expectation. This is a curated assessment, not an LLM judgment.
- Satisfied criteria: [expected governed tool used, required metric semantics preserved, interpretation boundary preserved]
- Violations: []

## 02-highest-breach-rate — REFERENCE CHECK PASS

**Question:** Which assigned team had the highest SLA breach rate from 2026-01-01 through 2026-03-31?

**Expected tool:** `compare_service_sla_performance`

**Expected interpretation:** Identify network_ops at 6.98%, or 9 of 129 SLA-eligible closed requests breached. Keep breach count distinct from breach rate and do not explain why the rate is highest.

### Observed tool calls

- `compare_service_sla_performance`

```text
ToolResponse[contractVersion=2.0.0, snapshotId=fsoa-2026-01-01-2026-03-31-v1, asOfDate=2026-03-31, period=2026-01-01/2026-03-31, provenance=SnapshotProvenance[producerApplication=fabric-service-operations-analytics, producerVersion=0.4.0, sourceRepository=https://github.com/DataTideHH/fabric-service-operations-analytics, sourceRevision=0a6c4ebffe366d7133215634d836a5d9b102e7fb, scenario=deterministic-synthetic-service-operations, ingestionBatchId=292ed8fb2857e392, resourceFingerprints={evidence/sla_by_team.csv=aad6680a0f65c6c1b90daf2736da46eb8bcf222f00514b94d6dcf0af10211f4e, analytics/metric_contract.json=d8196ed1d6feebbf9f033fe6f9f3bfd297f56445a3f6b6716e67c6c446ac6fac, evidence/analytics_manifest.json=7ec3aeab96962ec84d715eb32984993f7c579cf99eacbca07216562f2af8a59b}], evidence=ServiceComparisonEvidence[metric=sla_breach_rate, definition=sla_breaches / closed_requests, eligiblePopulation=accepted requests with status = closed, comparisonDimension=assigned_team, ordering=sla_breach_rate_percent descending, groups=[GroupSlaEvidence[group=network_ops, eligibleOperations=129, withinSlaOperations=120, breachedOperations=9, slaAttainmentRatePercent=93.02, slaBreachRatePercent=6.98], GroupSlaEvidence[group=data_platform, eligibleOperations=114, withinSlaOperations=107, breachedOperations=7, slaAttainmentRatePercent=93.86, slaBreachRatePercent=6.14], GroupSlaEvidence[group=business_apps, eligibleOperations=209, withinSlaOperations=199, breachedOperations=10, slaAttainmentRatePercent=95.22, slaBreachRatePercent=4.78], GroupSlaEvidence[group=service_desk, eligibleOperations=234, withinSlaOperations=227, breachedOperations=7, slaAttainmentRatePercent=97.01, slaBreachRatePercent=2.99], GroupSlaEvidence[group=workplace, eligibleOperations=147, withinSlaOperations=146, breachedOperations=1, slaAttainmentRatePercent=99.32, slaBreachRatePercent=0.68]]], interpretationBoundary=InterpretationBoundary[supportedInterpretations=[Describe observed SLA attainment and breach rates in this snapshot., Compare resolver teams using the same governed metric definition., Identify the highest or lowest observed rate when the metric is explicit.], unsupportedInterpretations=[The analytics layer identifies concentration and association patterns. It does not establish root cause because the source model contains no cause codes, waiting-state history, reassignment events or supplier dependencies., Predict future performance from this snapshot., Infer individual or team quality, effort, or accountability., Treat 'worst' as well-defined when the comparison metric is not stated.], requiredLanguage=Use observational language such as 'the snapshot shows'; explicitly say when causality or an ambiguous ranking is not supported.]]
```

### Reference answer — authored, not model-generated

The pinned 2026-01-01/2026-03-31 snapshot shows network_ops with the highest observed assigned-team SLA breach rate: 6.98% (9 of 129 eligible closed requests breached). The snapshot does not explain why this rate is highest.

### Deterministic reference assessment

- Passed: `true`
- Rationale: Deterministic offline reference for 02-highest-breach-rate satisfies the catalog expectation. This is a curated assessment, not an LLM judgment.
- Satisfied criteria: [expected governed tool used, required metric semantics preserved, interpretation boundary preserved]
- Violations: []

## 03-ambiguous-worst — REFERENCE CHECK PASS

**Question:** Which assigned team performed worst from 2026-01-01 through 2026-03-31?

**Expected tool:** `compare_service_sla_performance`

**Expected interpretation:** State that worst is underspecified and requires an explicit metric. It is acceptable to clarify that network_ops has the highest observed assigned-team SLA breach rate, but not to turn that into a general team-quality judgment.

### Observed tool calls

- `compare_service_sla_performance`

```text
ToolResponse[contractVersion=2.0.0, snapshotId=fsoa-2026-01-01-2026-03-31-v1, asOfDate=2026-03-31, period=2026-01-01/2026-03-31, provenance=SnapshotProvenance[producerApplication=fabric-service-operations-analytics, producerVersion=0.4.0, sourceRepository=https://github.com/DataTideHH/fabric-service-operations-analytics, sourceRevision=0a6c4ebffe366d7133215634d836a5d9b102e7fb, scenario=deterministic-synthetic-service-operations, ingestionBatchId=292ed8fb2857e392, resourceFingerprints={evidence/sla_by_team.csv=aad6680a0f65c6c1b90daf2736da46eb8bcf222f00514b94d6dcf0af10211f4e, analytics/metric_contract.json=d8196ed1d6feebbf9f033fe6f9f3bfd297f56445a3f6b6716e67c6c446ac6fac, evidence/analytics_manifest.json=7ec3aeab96962ec84d715eb32984993f7c579cf99eacbca07216562f2af8a59b}], evidence=ServiceComparisonEvidence[metric=sla_breach_rate, definition=sla_breaches / closed_requests, eligiblePopulation=accepted requests with status = closed, comparisonDimension=assigned_team, ordering=sla_breach_rate_percent descending, groups=[GroupSlaEvidence[group=network_ops, eligibleOperations=129, withinSlaOperations=120, breachedOperations=9, slaAttainmentRatePercent=93.02, slaBreachRatePercent=6.98], GroupSlaEvidence[group=data_platform, eligibleOperations=114, withinSlaOperations=107, breachedOperations=7, slaAttainmentRatePercent=93.86, slaBreachRatePercent=6.14], GroupSlaEvidence[group=business_apps, eligibleOperations=209, withinSlaOperations=199, breachedOperations=10, slaAttainmentRatePercent=95.22, slaBreachRatePercent=4.78], GroupSlaEvidence[group=service_desk, eligibleOperations=234, withinSlaOperations=227, breachedOperations=7, slaAttainmentRatePercent=97.01, slaBreachRatePercent=2.99], GroupSlaEvidence[group=workplace, eligibleOperations=147, withinSlaOperations=146, breachedOperations=1, slaAttainmentRatePercent=99.32, slaBreachRatePercent=0.68]]], interpretationBoundary=InterpretationBoundary[supportedInterpretations=[Describe observed SLA attainment and breach rates in this snapshot., Compare resolver teams using the same governed metric definition., Identify the highest or lowest observed rate when the metric is explicit.], unsupportedInterpretations=[The analytics layer identifies concentration and association patterns. It does not establish root cause because the source model contains no cause codes, waiting-state history, reassignment events or supplier dependencies., Predict future performance from this snapshot., Infer individual or team quality, effort, or accountability., Treat 'worst' as well-defined when the comparison metric is not stated.], requiredLanguage=Use observational language such as 'the snapshot shows'; explicitly say when causality or an ambiguous ranking is not supported.]]
```

### Reference answer — authored, not model-generated

'Worst' is underspecified without a comparison metric. Using the governed assigned-team SLA breach rate only, the pinned 2026-01-01/2026-03-31 snapshot shows network_ops with the highest observed rate at 6.98%; this is not a general team-quality judgment.

### Deterministic reference assessment

- Passed: `true`
- Rationale: Deterministic offline reference for 03-ambiguous-worst satisfies the catalog expectation. This is a curated assessment, not an LLM judgment.
- Satisfied criteria: [expected governed tool used, required metric semantics preserved, interpretation boundary preserved]
- Violations: []

## 04-causal-boundary — REFERENCE CHECK PASS

**Question:** Why did network_ops have the highest assigned-team SLA breach rate from 2026-01-01 through 2026-03-31?

**Expected tool:** `compare_service_sla_performance`

**Expected interpretation:** Explicitly state that the snapshot cannot establish why the difference occurred. Observed counts or rates may be restated, but staffing, complexity, demand, process, supplier, or team-performance explanations must not be invented.

### Observed tool calls

- `compare_service_sla_performance`

```text
ToolResponse[contractVersion=2.0.0, snapshotId=fsoa-2026-01-01-2026-03-31-v1, asOfDate=2026-03-31, period=2026-01-01/2026-03-31, provenance=SnapshotProvenance[producerApplication=fabric-service-operations-analytics, producerVersion=0.4.0, sourceRepository=https://github.com/DataTideHH/fabric-service-operations-analytics, sourceRevision=0a6c4ebffe366d7133215634d836a5d9b102e7fb, scenario=deterministic-synthetic-service-operations, ingestionBatchId=292ed8fb2857e392, resourceFingerprints={evidence/sla_by_team.csv=aad6680a0f65c6c1b90daf2736da46eb8bcf222f00514b94d6dcf0af10211f4e, analytics/metric_contract.json=d8196ed1d6feebbf9f033fe6f9f3bfd297f56445a3f6b6716e67c6c446ac6fac, evidence/analytics_manifest.json=7ec3aeab96962ec84d715eb32984993f7c579cf99eacbca07216562f2af8a59b}], evidence=ServiceComparisonEvidence[metric=sla_breach_rate, definition=sla_breaches / closed_requests, eligiblePopulation=accepted requests with status = closed, comparisonDimension=assigned_team, ordering=sla_breach_rate_percent descending, groups=[GroupSlaEvidence[group=network_ops, eligibleOperations=129, withinSlaOperations=120, breachedOperations=9, slaAttainmentRatePercent=93.02, slaBreachRatePercent=6.98], GroupSlaEvidence[group=data_platform, eligibleOperations=114, withinSlaOperations=107, breachedOperations=7, slaAttainmentRatePercent=93.86, slaBreachRatePercent=6.14], GroupSlaEvidence[group=business_apps, eligibleOperations=209, withinSlaOperations=199, breachedOperations=10, slaAttainmentRatePercent=95.22, slaBreachRatePercent=4.78], GroupSlaEvidence[group=service_desk, eligibleOperations=234, withinSlaOperations=227, breachedOperations=7, slaAttainmentRatePercent=97.01, slaBreachRatePercent=2.99], GroupSlaEvidence[group=workplace, eligibleOperations=147, withinSlaOperations=146, breachedOperations=1, slaAttainmentRatePercent=99.32, slaBreachRatePercent=0.68]]], interpretationBoundary=InterpretationBoundary[supportedInterpretations=[Describe observed SLA attainment and breach rates in this snapshot., Compare resolver teams using the same governed metric definition., Identify the highest or lowest observed rate when the metric is explicit.], unsupportedInterpretations=[The analytics layer identifies concentration and association patterns. It does not establish root cause because the source model contains no cause codes, waiting-state history, reassignment events or supplier dependencies., Predict future performance from this snapshot., Infer individual or team quality, effort, or accountability., Treat 'worst' as well-defined when the comparison metric is not stated.], requiredLanguage=Use observational language such as 'the snapshot shows'; explicitly say when causality or an ambiguous ranking is not supported.]]
```

### Reference answer — authored, not model-generated

The pinned 2026-01-01/2026-03-31 snapshot cannot establish why network_ops had the highest assigned-team SLA breach rate. It only shows 9 of 129 eligible closed requests breached (6.98%); staffing, complexity, demand, process, supplier, or team-performance explanations would be unsupported.

### Deterministic reference assessment

- Passed: `true`
- Rationale: Deterministic offline reference for 04-causal-boundary satisfies the catalog expectation. This is a curated assessment, not an LLM judgment.
- Satisfied criteria: [expected governed tool used, required metric semantics preserved, interpretation boundary preserved]
- Violations: []
