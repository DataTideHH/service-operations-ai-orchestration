# ADR 0002: Model-dependent evaluation is manual and auditable

## Status

Accepted for V2.

## Decision

V2 executes the four versioned cases only when `app.evaluation.enabled=true` and a chat provider is selected explicitly. OpenAI and Anthropic are supported through the portable Spring AI `ChatModel`/`ChatClient` APIs.

The answering client receives the two governed analytical tools. A separate judge client receives no tools and produces a structured assessment against each case's expected interpretation. The resulting Markdown report includes provider/model identity, tool traces, observed answers, and assessments.

The GitHub evaluation workflow is manual. It uploads the report even when evaluation fails and then reflects the failed result in the workflow status. It never commits generated evidence automatically.

## Consequences

- Normal CI remains deterministic and credential-free.
- Model behavior and judge behavior are both observational, not deterministic proof.
- Failed cases remain inspectable because report creation precedes failure signaling.
- Provider switching requires configuration rather than application-code changes.
- A human still decides whether a generated report should become committed evidence.
