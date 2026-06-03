# NeveraChef AI - Agent Instructions

## Purpose

Keep Codex fast and precise in this Android-first KMP / Compose Multiplatform repo.

## Default Behaviour

- Load the minimum context needed.
- Do not scan the repository by default.
- Inspect only files directly related to the task.
- Do not open `ai/**` docs unless the task needs that specific topic.
- Do not refactor, redesign architecture, add dependencies or touch unrelated files unless requested.
- Preserve existing Kotlin, Compose, KMP and navigation patterns.
- Keep responses short.
- Validate with the smallest useful command when practical.

## Context Loading

Start with:

1. User request.
2. Direct target file or screen.
3. Direct dependencies only if needed.

Open extra docs only by trigger:

| Trigger | Open |
|---|---|
| Repo layout / where to look | `ai/context/repository-map.md` |
| Architecture, modules, source sets, dependency direction | `ai/context/architecture.md` |
| Product terms, inventory semantics, shopping behaviour | `ai/context/product.md` |
| Stack choices, libraries, default tooling | `ai/context/stack.md` |
| Validation command uncertainty | `ai/rules/validation-android.md` |
| Git / PR / branch work | `ai/rules/git.md` |
| Screenshot-to-Compose or screen migration | `ai/skills/screen-migration/SKILL.md` |
| Compose screen review | `ai/skills/compose-screen-review/SKILL.md` |

Do not open index files such as `ai/context/README.md`, `ai/rules/README.md` or `ai/skills/README.md` unless you need to discover available docs.

## Repository Map

- `androidApp/` - Android host and framework integration.
- `iosApp/` - iOS host.
- `shared/` - KMP shared code and Compose UI.
- `shared/src/commonMain/kotlin/es/neverachefai/feature/` - feature code.
- `shared/src/commonMain/kotlin/es/neverachefai/core/` - shared design system, UI and infrastructure.
- `ai/context/` - optional background docs.
- `ai/rules/` - optional workflow and validation rules.
- `ai/skills/` - optional task-specific workflows.
- `ai/workflows/` - project decisions and implementation records.

## Android / Kotlin / KMP

- Keep business logic out of Composables.
- Keep `shared/commonMain` platform-agnostic.
- Use existing design tokens and components where practical.
- Use `expect/actual` only for real platform differences.
- Do not call persistence, preferences or AI providers directly from screens.
- Do not introduce new libraries unless explicitly requested.

## Validation

Use the smallest relevant command:

- Android app or shared UI change: `./gradlew :androidApp:assembleDebug`
- Shared Kotlin/domain change: `./gradlew :shared:compileKotlinAndroid`
- Documentation-only change: no Gradle command required.

Never claim validation that was not run.

## Final Output

Use this compact format:

```text
Files:
- path

Done:
- result

Check:
- command or Not run

Risk:
- Low / Medium / High
```
