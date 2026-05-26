# NeveraChef AI - Architecture Context

> **Version:** 2026-05-26  
> **Scope:** Architecture, ownership and dependency direction for NeveraChef AI.

Use this file only when a task needs architecture context. Use `AGENTS.md` for root execution rules.

## Intent

NeveraChef AI is an Android-first Kotlin Multiplatform / Compose Multiplatform app.

- Keep the architecture simple, local-first and production-oriented.
- Prefer explicit, testable code over abstract layers created only for symmetry.
- Preserve source-set boundaries and dependency direction.
- Keep UI, domain, persistence and platform concerns separate.

## Decisions

| Area | Decision |
|---|---|
| App shape | Android-first KMP / Compose Multiplatform |
| Shared module | `shared/` owns common product logic and shared Compose when practical |
| Hosts | `androidApp/` and `iosApp/` own platform host integration |
| Architecture | Pragmatic Clean Architecture with MVVM/MVI-style state where useful |
| Persistence | Local-first for inventory, pantry, recipes and shopping data when required |
| AI | Provider/repository abstraction; UI never calls providers directly |

## Related Files

| File | Purpose |
|---|---|
| `AGENTS.md` | Root agent rules |
| `ai/PRODUCT_CONTEXT.md` | Product scope and product invariants |
| `ai/VALIDATION_ANDROID.md` | Android/KMP validation |
| `ai/WORKFLOW_FEATURE.md` | Active feature progress |
| `ai/skills/*/SKILL.md` | Task-specific execution rules |

## Source Ownership

| Source set / module | Owns | Must not own |
|---|---|---|
| `shared/commonMain` | Shared Kotlin, shared Compose, domain models, state, events and common contracts | Android/iOS framework APIs, platform storage details, JVM-only APIs unless targets allow them |
| `shared/androidMain` | Android implementations for shared contracts | Shared product logic that belongs in `commonMain` |
| `shared/iosMain` | iOS implementations for shared contracts | Shared product logic that belongs in `commonMain` |
| `androidApp/` | Android host, activity/app wiring and Android integration | Shared business rules or reusable feature internals |
| `iosApp/` | iOS host and Swift integration | Shared business rules or reusable feature internals |

Rules:

- Put common behaviour in `commonMain` only when it works for all supported targets.
- Use `expect/actual` only for real platform differences requiring a shared contract.
- Do not move code to shared only because KMP allows it.
- Do not import Android/iOS framework APIs from `shared/commonMain`.

## Package Responsibilities

| Package | Responsibility |
|---|---|
| `app/` | Shared app composition root: theme, navigation and top-level state |
| `core/designsystem/` | Theme, colors, typography, spacing, shapes and design primitives |
| `core/ui/` | Reusable shared UI components when reuse is real |
| `core/persistence/` | Local storage APIs and implementations |
| `core/preferences/` | User settings and preferences storage |
| `domain/model/` | Pure domain models |
| `domain/usecase/` | Business actions, validation and orchestration when useful |
| `domain/repository/` | Repository contracts |
| `feature/*/` | Feature UI, state, events and presentation logic |
| `feature/navigation/` | Shared routes and navigation graph when applicable |

Rules:

- Do not create empty folders just to match an ideal structure.
- Add `domain/repository` or `domain/usecase` only when abstraction or business logic is useful.
- `core/persistence` and `core/preferences` must not be accessed directly from Composables.
- Keep feature code grouped by product area.
- Keep components in the feature until reuse is real.

## Dependency Direction

Preferred flow:

```text
Screen -> Event -> ViewModel/StateHolder -> UseCase -> Repository contract -> Data source
Screen <- State <- ViewModel/StateHolder <- Result  <- Repository          <- Data source
```

Allowed dependencies:

```text
app -> feature
feature -> domain model/usecase/repository contracts
feature -> core ui/designsystem
core persistence/preferences -> domain models/contracts when needed
platform source sets -> shared contracts
```

Forbidden dependencies:

```text
Screen -> persistence/preferences/AI provider directly
Domain -> Compose
Domain -> Android/iOS framework
Feature A -> Feature B internals
shared/commonMain -> android.* / UIKit / Swift-only APIs
```

## Feature Shape

Use this shape for new or meaningfully reworked features only when it improves clarity:

```text
feature/<featureName>/
├── <FeatureName>Route.kt
├── <FeatureName>Screen.kt
├── <FeatureName>State.kt
├── <FeatureName>Event.kt
├── <FeatureName>Effect.kt        Optional
└── <FeatureName>ViewModel.kt     Only when needed
```

Do not create `Route`, `State`, `Event`, `Effect` or `ViewModel` files for screens too small to justify them.

## Compose And State

- Composables render state and emit events.
- UI state must be immutable and render-ready.
- Use events for user intent, not direct mutation from UI.
- Use effects only for one-off actions such as navigation, snackbar, toast or permission prompts.
- Collect effects through lifecycle-aware collection where available.
- Keep persistence, preferences, network and AI provider calls outside Composables.
- Keep design tokens and primitives in `core/designsystem/`.

## Domain, Persistence And AI

Domain:

- Pure Kotlin only.
- Owns models, validation, useful use cases and repository contracts.
- No Compose, platform APIs or persistence implementation details.

Persistence:

- Local storage is the source of truth for persisted inventory and shopping data.
- UI state is not long-term storage.
- Keep storage details behind repository/data/persistence APIs.
- Mention migration or data-loss risk when changing schemas.
- Do not add remote sync, accounts, cloud backup or backend persistence unless requested.

AI:

- UI must not call provider SDKs/APIs directly.
- Feature code depends on provider/repository contracts.
- Provider DTOs must not leak into UI state.
- API keys and tokens must never be hardcoded or committed.
- Demo AI responses must be clearly named sample/demo/test data.

## Navigation

- Keep routes stable and explicit.
- Prefer simple args: IDs, enums or primitive values.
- Do not pass large mutable objects between screens.
- Keep navigation side effects outside `Screen` where practical.
- Use one-off effects when navigation is driven by state holder logic.

## Testing

| Target | Preferred test |
|---|---|
| Domain models/value objects | Pure Kotlin unit tests |
| Use cases | Unit tests with small fakes |
| Mappers | Input-output unit tests |
| State holders/ViewModels | Event/state Flow tests |
| Repositories | Unit or integration tests depending on storage |
| AI provider/repository mapping | Contract tests or mapper unit tests with fake providers |

Rules:

- Prefer behaviour tests over structure tests.
- Test business rules before visual details.
- Add regression tests for logic bugs.
- Do not add heavy UI test infrastructure unless requested or already present.

## Architecture Change Checklist

Before editing:

1. Inspect the closest existing feature/package.
2. Inspect related state, events, route and screen files.
3. Inspect domain/persistence/platform code only if the task touches those concerns.
4. Make the smallest safe change.

Before finishing:

1. Check for platform leaks in `commonMain`.
2. Check business logic is not inside Composables.
3. Check persistence/preferences/AI providers are not called directly from Screens.
4. Check UI state is immutable.
5. Validate using `ai/VALIDATION_ANDROID.md`.
