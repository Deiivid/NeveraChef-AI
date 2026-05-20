# NeveraChef AI - Feature workflow

## Objective

Implement features in small, verifiable steps.

## Execution phases

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
- Run Gradle build.
- Fix compile errors only.
- Do not introduce unrelated changes.

### Phase 4 - Visual check
- If emulator/device is available:
    - install debug build
    - open the target screen
    - capture screenshot
    - compare with reference
- Adjust spacing, colors, typography and layout only.

### Phase 5 - Report
Final output must be:

Files changed:
- ...

Validation:
- command run + result

Notes:
- relevant caveats only