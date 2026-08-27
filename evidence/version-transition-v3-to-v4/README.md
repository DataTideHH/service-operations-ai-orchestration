# V3-to-V4 compatibility evidence

This directory records the intentional comparison failure between the historical V3 baseline and the V4 governed analytics handoff baseline.

Both baselines pass all four cases, but they are not regression-comparable because V4 changes the governed snapshot, adds the producer JSON Schema to the manifest, and updates the evaluation catalog. The comparison command therefore returns a non-zero exit code and reports `governed resource fingerprints differ`. This is the expected fail-closed behavior.

File:

- `evaluation-comparison.md`: generated incompatibility report
