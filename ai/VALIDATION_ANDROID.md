# NeveraChef AI - Android Validation

> **Version:** 2026-05-26  
> **Scope:** Android/KMP build, test, lint and visual validation.

Use this file when a task changes Android code, shared KMP code used by Android, Compose UI, Gradle, tests, lint or Detekt.

## Rule

Run the smallest useful validation command for the touched area.

Never claim a build, test, lint, Detekt, emulator, preview, screenshot or manual check was executed if it was not.

## Discovery

If module names are uncertain:

```bash
./gradlew projects
```

Use real module names shown by Gradle.

## Common Commands

| Purpose | Command |
|---|---|
| List modules | `./gradlew projects` |
| Compile shared Kotlin for Android | `./gradlew :shared:compileKotlinAndroid` |
| Build Android debug app | `./gradlew :androidApp:assembleDebug` |
| Run unit tests | `./gradlew test` |
| Run Android lint debug variant | `./gradlew :androidApp:lintDebug` |
| Run global lint, if configured | `./gradlew lint` |
| Run Detekt, if configured | `./gradlew detekt` |
| Run checks, if configured | `./gradlew check` |

If a command is not configured, report it as unavailable. Do not count it as passed.

## Validation By Change Type

| Change type | Minimum useful validation |
|---|---|
| Documentation only | No Gradle command required; report not run with reason |
| Shared domain/model/use case | `./gradlew :shared:compileKotlinAndroid` + relevant tests if present |
| Shared Compose UI | `./gradlew :shared:compileKotlinAndroid` + `./gradlew :androidApp:assembleDebug` |
| Android app UI | `./gradlew :androidApp:assembleDebug` |
| Android host/app wiring | `./gradlew :androidApp:assembleDebug` |
| Navigation | `./gradlew :shared:compileKotlinAndroid` + `./gradlew :androidApp:assembleDebug` |
| Persistence/preferences | `./gradlew :shared:compileKotlinAndroid` + relevant tests if present |
| AI provider/repository contract | `./gradlew :shared:compileKotlinAndroid` + relevant tests if present |
| Gradle/build config | `./gradlew projects` + `./gradlew :androidApp:assembleDebug` |
| Dependency/plugin change | `./gradlew :androidApp:assembleDebug` + `./gradlew test` |
| Test code | Affected test task, or `./gradlew test` when unsure |
| Lint config | `./gradlew :androidApp:lintDebug` or `./gradlew lint` |
| Detekt config | `./gradlew detekt` |
| Large cross-cutting Android/KMP change | Full sequence below |

## Full Sequence

Use before marking a large Android/KMP task as PR-ready:

```bash
./gradlew :shared:compileKotlinAndroid
./gradlew :androidApp:assembleDebug
./gradlew :androidApp:lintDebug
./gradlew test
./gradlew detekt
```

If one command fails, stop unless the user asked to continue collecting failures.

## UI Validation

For UI changes:

1. Build the affected module/app first.
2. Capture previews/screenshots only if the environment supports it.
3. Compare against the request, reference image or previous state.
4. Report exactly what was visually checked.

Do not claim Android Studio, emulator, Compose Preview or screenshot validation unless actually used.

## Agent Rules

- Prefer targeted validation over broad expensive validation.
- Do not hide failing build, test, lint or Detekt output.
- Do not edit unrelated files to make validation pass.
- Do not change Gradle files only to make validation easier unless requested.
- Do not modify iOS files for Android-only validation failures.
- If validation was required but not run, say so directly.

## Report Format

Passed:

```text
Validation:
- Ran: ./gradlew :androidApp:assembleDebug
- Result: Passed
```

Not run:

```text
Validation:
- Not run. Reason: documentation-only change.
```

Failed:

```text
Validation:
- Ran: ./gradlew :androidApp:assembleDebug
- Result: Failed
- Error: <short relevant error>
```

Multiple commands:

```text
Validation:
- Ran: ./gradlew :shared:compileKotlinAndroid - Passed
- Ran: ./gradlew :androidApp:assembleDebug - Passed
- Ran: ./gradlew test - Failed
- Error: <short relevant error>
```
