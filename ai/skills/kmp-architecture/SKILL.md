# KMP Architecture Skill

Use this skill when creating, reviewing or evolving architecture in NeveraChef AI.

## Use For

- KMP source-set boundary decisions
- feature package structure
- state/event/effect architecture
- domain/repository/use case placement
- platform-specific implementation placement

Do not use for visual-only, Gradle-only or GitHub-only tasks.

## Sources

| Source | Use |
|---|---|
| Repository code | Final authority for current conventions |
| `AGENTS.md` | Root agent rules |
| `ai/context/architecture.md` | Architecture ownership and dependency direction |
| `ai/context/product.md` | Product invariants when behaviour is touched |
| `ai/rules/validation-android.md` | Validation command selection |

## Baseline

| Layer | Responsibility |
|---|---|
| `app/` | Composition root, theme and navigation setup |
| `feature/*` | Screen UI, route wiring, state, events, effects and presentation logic |
| `domain/*` | Pure models, use cases and repository contracts when useful |
| `core/ui` / `core/designsystem` | Shared UI primitives, theme and design tokens |
| `core/persistence` / `core/preferences` | Local-first storage and settings behind APIs |
| Platform source sets | Android/iOS implementations only |

## Feature Shape

Use only when it improves clarity:

```text
feature/<featureName>/
├── <FeatureName>Route.kt
├── <FeatureName>Screen.kt
├── <FeatureName>State.kt
├── <FeatureName>Event.kt
├── <FeatureName>Effect.kt        Optional
└── <FeatureName>ViewModel.kt     Only when needed
```

Do not create architecture files just for symmetry.

## Rules

- Screens render immutable state and emit events.
- State holders/ViewModels own UI logic when complexity requires it.
- Domain stays pure Kotlin and platform independent.
- Use cases are for real business behaviour, validation or orchestration.
- Repository contracts hide persistence/preferences/network/provider details.
- Common code must not depend on Android `Context`, `Application`, Activity, Fragment, UIKit or Swift-only types.
- Platform access from shared code requires a shared contract plus platform implementation.
- Keep feature internals private to that feature.

## Forbidden

```text
Screen -> persistence/preferences/AI provider directly
Domain -> Compose
Domain -> Android/iOS APIs
Feature A -> Feature B internals
shared/commonMain -> android.* / UIKit / Swift-only APIs
```

## Checklist

Before editing:

1. Inspect the closest existing feature/package.
2. Inspect related state, events, route and screen files.
3. Inspect domain/persistence/platform code only if involved.
4. Make the smallest architecture change that preserves current conventions.

Before finishing:

1. Check for platform leaks.
2. Check business logic is outside Composables.
3. Check UI state is immutable and render-ready.
4. Validate using `ai/rules/validation-android.md`.

## Output

```text
Files changed:
- ...

Validation:
- ...

Notes:
- ...
```
