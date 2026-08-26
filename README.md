# Service Operations AI Orchestration

A Spring AI application for governed interpretation and repeatable LLM evaluation of service-operations analytics.

> This project evaluates whether an LLM preserves metric semantics and evidence boundaries when consuming governed analytical results.

V1 exposes a deliberately pinned snapshot from `fabric-service-operations-analytics` through exactly two read-only Spring AI tools. Tool results contain the contract version, snapshot identity, reporting period, evidence, and an explicit `interpretationBoundary`. There is no database, ORM, JDBC, ML pipeline, RAG system, vector store, frontend, or live analytics dependency.

## Tools

| Tool | Governed question |
| --- | --- |
| `get_overall_sla_performance` | Overall SLA attainment, breach rate, and operation counts |
| `compare_service_sla_performance` | Service-level comparison using the same governed SLA definition |

Both tools return counts and derived rates. The service comparison is ordered by observed breach rate, but its boundary explicitly rejects causal explanations and an undefined use of “worst.”

## Run deterministic checks

Requirement: Java 21+. The repository includes the Maven Wrapper.

```powershell
.\mvnw.cmd clean verify
```

These tests do not call an LLM. They verify snapshot reconciliation, rate derivation, the exact two-tool surface, contract metadata, ordering, and the presence of the interpretation boundary.

## V2: run the four model evaluations

V2 includes OpenAI and Anthropic provider starters but selects no provider by default, so ordinary startup and CI still require no credentials. Enable one provider only for an explicit evaluation run.

The LLM evaluation is fully prepared but has not yet been executed against a live provider because no API credits were configured.

OpenAI example:

```powershell
$env:SPRING_AI_OPENAI_API_KEY="..."
java -jar target/service-operations-ai-orchestration-0.3.0-SNAPSHOT.jar `
  --app.evaluation.enabled=true `
  --app.evaluation.provider=openai `
  --app.evaluation.model=gpt-5-mini `
  --spring.ai.model.chat=openai `
  --spring.ai.openai.chat.options.model=gpt-5-mini
```

Anthropic uses `SPRING_AI_ANTHROPIC_API_KEY`, `--spring.ai.model.chat=anthropic`, and `--spring.ai.anthropic.chat.options.model=<model>`.

Each case receives an answer from a client with the two governed tools. A separate client, with no tools, returns a structured automatic assessment. The runner captures tool calls and results, writes `evidence/runs/evaluation-report.md` and `evidence/runs/evaluation-report.json`, validates the JSON artifact, and fails after report creation if any case fails.

The manual GitHub workflow accepts `provider` and `model` inputs. Configure `OPENAI_API_KEY` and/or `ANTHROPIC_API_KEY` as repository secrets before dispatching it. The generated report is always uploaded as a workflow artifact; it is not committed automatically.

Real model observations belong under [`evidence/`](evidence/README.md); they remain separate from normal CI.

## V3: governed and reproducible evaluation artifacts

V3 versions the answer and judge prompts under `src/main/resources/prompts/`. Every JSON and Markdown report records:

- schema and application versions
- provider, model, source revision, and execution time
- explicit prompt versions
- SHA-256 fingerprints for the analytics snapshot, evaluation catalog, and all prompts
- the four answers, tool traces, and structured assessments

JSON reports can be validated without credentials or an LLM call:

```powershell
java -jar target/service-operations-ai-orchestration-0.3.0-SNAPSHOT.jar `
  --app.evaluation.artifact-command=validate `
  --app.evaluation.artifact-report=evidence/runs/evaluation-report.json
```

Two reports can be compared offline when their governed resource fingerprints match:

```powershell
java -jar target/service-operations-ai-orchestration-0.3.0-SNAPSHOT.jar `
  --app.evaluation.artifact-command=compare `
  --app.evaluation.artifact-baseline=evidence/runs/evaluation-baseline.json `
  --app.evaluation.artifact-report=evidence/runs/evaluation-report.json
```

The comparison writes `evidence/runs/evaluation-comparison.md`. It fails for incompatible governed inputs or when a baseline pass becomes a candidate failure. Validation and comparison are deterministic and cost-free.

## Pinned evidence

The source snapshot is [`service-operations-snapshot-v1.properties`](src/main/resources/analytics/service-operations-snapshot-v1.properties). Rates are derived from integer counts in Java so a stored percentage cannot disagree with its numerator and denominator. Updating evidence is a versioned change: replace the snapshot, reconcile totals, and revise the contract version when semantics change.

## Scope

V1 includes Java 21, Spring Boot, Spring AI, two read-only tools, pinned evidence and metric contract, four evaluation cases, deterministic tests, and CI.

V2 adds an executable provider-configurable evaluation runner, captured tool traces, structured automatic assessment, auditable Markdown reports, and a manual GitHub workflow.

V3 adds versioned prompts, run manifests, governed resource fingerprints, machine-readable JSON reports, deterministic artifact validation, and offline regression comparison.

Explicitly excluded: database access, ORM, JDBC, ML, Python, RAG, pgvector, Power BI, extra tools, a frontend, and a broad roadmap.

## License

This project is licensed under the MIT License. See the [`LICENSE`](LICENSE) file for details.
