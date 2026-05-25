# Android validation

## Discovery

Before building, inspect Gradle modules:

```bash
./gradlew projects
# Android validation

Use this file when an AI agent, Codex task or developer needs to validate Android-specific changes in this KMP project.

## Purpose

Validate that Android code still builds, compiles and follows the expected quality checks after a change.

This file is Android-focused. For shared/KMP architecture or feature rules, use `.ai/ARCHITECTURE_CONTEXT.md` and the relevant skill file.

---

## Default Android validation

Run the most specific command that validates the change.

| Command | Purpose |
|---|---|
| `./gradlew :androidApp:assembleDebug` | Builds the Android app debug variant. |
| `./gradlew :shared:compileKotlinAndroid` | Compiles shared KMP code for Android. |
| `./gradlew :androidApp:lintDebug` | Runs Android lint for the debug variant when available. |
| `./gradlew test` | Runs available unit tests. |
| `./gradlew :shared:testDebugUnitTest` | Runs shared Android unit tests when configured. |
| `./gradlew detekt` | Runs Detekt when configured. |

---

## Recommended validation by change type

| Change type | Minimum validation |
|---|---|
| Android UI only | `./gradlew :androidApp:assembleDebug` |
| Shared Compose UI | `./gradlew :shared:compileKotlinAndroid` + `./gradlew :androidApp:assembleDebug` |
| Shared domain/model logic | `./gradlew :shared:compileKotlinAndroid` + relevant tests |
| Persistence/preferences | `./gradlew :shared:compileKotlinAndroid` + relevant tests |
| Gradle/build files | `./gradlew projects` + `./gradlew :androidApp:assembleDebug` |
| Dependency changes | `./gradlew :androidApp:assembleDebug` + `./gradlew test` |
| Lint/style changes | `./gradlew :androidApp:lintDebug` or `./gradlew lint` |
| Detekt/config changes | `./gradlew detekt` |

---

## Full Android check

Use this before marking a larger Android task as PR-ready:

```bash
./gradlew :shared:compileKotlinAndroid
./gradlew :androidApp:assembleDebug
./gradlew :androidApp:lintDebug
./gradlew test
./gradlew detekt
```

If a command is not configured in the project, report it clearly instead of pretending it passed.

---

## Agent rules

- Do not claim validation was executed if it was not.
- Prefer targeted validation over broad expensive validation.
- If a command fails, report the failing command and the relevant error.
- Do not hide failing tests, lint, detekt or build errors.
- Do not change Gradle files just to make validation easier unless explicitly requested.
- Do not modify iOS files for Android-only validation failures.
- If module names differ, run `./gradlew projects` and adapt commands to the real module names.

---

## Validation report format

Use this format in the final response:

```text
Validation:
- Ran: ./gradlew :androidApp:assembleDebug
- Result: Passed
```

If not run:

```text
Validation:
- Not run. Reason: documentation-only change.
```

If failed:

```text
Validation:
- Ran: ./gradlew :androidApp:assembleDebug
- Result: Failed
- Error: <short relevant error>
```