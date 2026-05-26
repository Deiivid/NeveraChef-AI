# NeveraChef AI — Android Validation

> **Version:** 2026-05-26  
> **Scope:** Android/KMP validation rules for NeveraChef AI.  
> **Audience:** AI agents, Codex tasks and developers validating Android-facing changes.

Use this file when a task changes Android code, shared KMP code used by Android, Compose UI, Gradle configuration, tests, lint, Detekt or anything that can affect the Android build.

For architecture and package ownership, use `.ai/ARCHITECTURE_CONTEXT.md`.  
For root agent behaviour, use `AGENTS.md`.

---

## Core rule

Run the smallest useful validation command for the touched area.

Never claim that a build, test, lint, Detekt, emulator, preview, screenshot or manual check was executed if it was not.

If validation is skipped, unavailable or fails, report it clearly.

---

## Discovery

Before running module-specific commands, inspect available Gradle modules when module names are uncertain:

```bash
./gradlew projects
```

Use the real module names shown by Gradle. Do not invent module paths.

---

## Common commands

| Purpose | Command |
|---|---|
| List Gradle modules | `./gradlew projects` |
| Compile shared Kotlin for Android | `./gradlew :shared:compileKotlinAndroid` |
| Build Android debug app | `./gradlew :androidApp:assembleDebug` |
| Run all available unit tests | `./gradlew test` |
| Run Android lint debug variant | `./gradlew :androidApp:lintDebug` |
| Run global Android lint, if configured | `./gradlew lint` |
| Run Detekt, if configured | `./gradlew detekt` |
| Run checks, if configured | `./gradlew check` |

If a command is not configured in the repository, report it as unavailable instead of treating it as passed.

---

## Validation by change type

| Change type | Minimum useful validation |
|---|---|
| Documentation-only change | No Gradle command required. Report validation as not run with reason. |
| Shared domain/model/use case | `./gradlew :shared:compileKotlinAndroid` + relevant tests if present |
| Shared Compose UI | `./gradlew :shared:compileKotlinAndroid` + `./gradlew :androidApp:assembleDebug` |
| Android app UI only | `./gradlew :androidApp:assembleDebug` |
| Android host/activity/app wiring | `./gradlew :androidApp:assembleDebug` |
| Navigation changes | `./gradlew :shared:compileKotlinAndroid` + `./gradlew :androidApp:assembleDebug` |
| Persistence/preferences | `./gradlew :shared:compileKotlinAndroid` + relevant tests if present |
| AI provider/repository contract | `./gradlew :shared:compileKotlinAndroid` + relevant tests if present |
| Gradle/build configuration | `./gradlew projects` + `./gradlew :androidApp:assembleDebug` |
| Dependency or plugin changes | `./gradlew :androidApp:assembleDebug` + `./gradlew test` |
| Test code changes | Run the affected test task, or `./gradlew test` when unsure |
| Lint configuration | `./gradlew :androidApp:lintDebug` or `./gradlew lint` |
| Detekt configuration | `./gradlew detekt` |
| Large cross-cutting Android change | Run the full Android validation sequence below |

---

## Full Android validation sequence

Use this before marking a large Android/KMP task as PR-ready:

```bash
./gradlew :shared:compileKotlinAndroid
./gradlew :androidApp:assembleDebug
./gradlew :androidApp:lintDebug
./gradlew test
./gradlew detekt
```

If one command fails, stop unless the user explicitly asked to continue collecting failures.

Report the failing command and the relevant error summary.

---

## Emulator, preview and screenshot validation

Web/cloud agents must not claim Android Studio, emulator, Compose Preview or screenshot validation unless those tools are actually available and used.

Local agents may use Android Studio, emulator, previews and screenshots when available.

For UI changes:

1. Build the app or affected module first.
2. Capture screenshots only if the environment supports it.
3. Compare screenshots against the requested UI, reference image or previous state.
4. Report what was visually checked.

If screenshots/previews are not available, say so directly.

---

## Agent rules

- Prefer targeted validation over broad expensive validation.
- Do not hide failing tests, lint, Detekt or build errors.
- If `lint`, `detekt`, `check` or a module-specific task is not configured, report it as unavailable/skipped with the command attempted or the reason it was not attempted.
- Do not treat an unavailable optional tool as a passed validation check.
- Do not edit unrelated files to make validation pass.
- Do not change Gradle files just to make validation easier unless explicitly requested.
- Do not modify iOS files for Android-only validation failures.
- Do not claim success based only on code inspection when a validation command was required but not run.
- If module names differ, run `./gradlew projects` and adapt commands to the real module names.
- If the task is documentation-only, validation may be skipped, but the final response must say it was skipped and why.

---

## Validation report format

Use this format in the final response when validation passed:

```text
Validation:
- Ran: ./gradlew :androidApp:assembleDebug
- Result: Passed
```

Use this format when validation was not run:

```text
Validation:
- Not run. Reason: documentation-only change.
```

Use this format when validation failed:

```text
Validation:
- Ran: ./gradlew :androidApp:assembleDebug
- Result: Failed
- Error: <short relevant error>
```

When multiple commands were run:

```text
Validation:
- Ran: ./gradlew :shared:compileKotlinAndroid — Passed
- Ran: ./gradlew :androidApp:assembleDebug — Passed
- Ran: ./gradlew test — Failed
- Error: <short relevant error>
```