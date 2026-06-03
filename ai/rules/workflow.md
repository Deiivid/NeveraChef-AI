# Feature Workflow

## Objective

Implement features in small, verifiable steps.

## Phases

### Phase 1 - Understand
- Inspect the relevant files.
- Identify the current architecture.
- Do not edit code yet.
- Report only:
  - files involved
  - intended changes
  - validation command

### Phase 2 - Implement
- Modify only the required files.
- Do not refactor unrelated code.
- Preserve existing state, callbacks and navigation.
- Use existing design system when available.

### Phase 3 - Validate
- Run the smallest useful build command.
- Fix compile errors only.
- Do not introduce unrelated changes.

### Phase 4 - Visual Check
- If emulator/device is available, install the debug build, open the target screen, capture a screenshot and compare it with the reference.
- Adjust spacing, colors, typography and layout only.

### Phase 5 - Report
Final output must be:

```text
Files changed:
- ...

Validation:
- command run + result

Notes:
- relevant caveats only
```
