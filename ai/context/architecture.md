# NeveraChef AI - Architecture Context

> **Version:** 2026-06-01
> **Scope:** Architecture, ownership and dependency direction.

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

Feature-specific direction:

- `pantry` and `shopping` are local-first features with full `data + domain + ui`.
- `recipes` exposes generation contracts in `domain` and uses `data` as the AI provider boundary.

## Related Files

| File | Purpose |
|---|---|
| `AGENTS.md` | Root agent rules |
| `ai/context/product.md` | Product scope and product invariants |
| `ai/rules/validation-android.md` | Android/KMP validation |
| `ai/rules/workflow.md` | Feature workflow |
| `ai/skills/README.md` | Task-specific execution rules |

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
| `feature/<name>/domain/model/` | Feature-owned pure domain models |
| `feature/<name>/domain/usecase/` | Feature-owned business actions, validation and orchestration |
| `feature/<name>/domain/<Feature>Repository.kt` | Feature repository contracts |
| `feature/<name>/data/` | Repository implementations, datasources, DTOs and mappers |
| `feature/<name>/ui/` | Feature screens, ViewModels, state, events and effects |
| `feature/navigation/` | Shared routes and navigation graph when applicable |

Rules:

- Do not create empty folders just to match an ideal structure.
- There is no global application `domain/` layer for feature-owned logic.
- Add feature `domain/usecase` only when abstraction or business logic is useful.
- `core/persistence` and `core/preferences` must not be accessed directly from Composables.
- Keep feature code grouped by product area.
- Keep components in the feature until reuse is real.

## Dependency Direction

Preferred flow:

```text
Screen -> Event -> ViewModel/StateHolder -> UseCase -> Repository contract <- Repository implementation <- Data source
Screen <- State <- ViewModel/StateHolder <- Result  <- UseCase            <- Repository contract       <- Repository implementation
```

Allowed dependencies:

```text
app -> feature
feature/<name>/ui -> feature/<name>/domain
feature/<name>/data -> feature/<name>/domain
feature/<name>/ui -> core ui/designsystem
feature/<name>/data -> core persistence/preferences when needed
platform source sets -> shared contracts
```

Forbidden dependencies:

```text
feature/<name>/ui -> feature/<name>/data
feature/<name>/data -> feature/<name>/ui
Screen -> persistence/preferences/AI provider directly
feature/<name>/domain -> Compose
feature/<name>/domain -> Android/iOS framework
Feature A -> Feature B internals
shared/commonMain -> android.* / UIKit / Swift-only APIs
```

Feature-layered rule:

```text
ui -> domain <- data
```

`ui` owns screens, routes, UI state, UI events, one-off effects and ViewModels/state holders.
`data` owns repository implementations, mappers, DTOs and datasources.
`domain` is per-feature and owns pure models, use cases, validation and repository contracts.

Local-first persistence must be accessed through feature `data` datasources. Composables and ViewModels must not know the concrete storage technology.

## Feature Shape

Use this shape for new or meaningfully reworked features only when it improves clarity:

```text
feature/<featureName>/
├── data/
│   ├── <FeatureName>RepositoryImpl.kt
│   ├── <FeatureName>LocalDataSource.kt
│   ├── dto/
│   └── mapper/
├── domain/
│   ├── <FeatureName>Repository.kt
│   ├── model/
│   └── usecase/
└── ui/
    ├── <FeatureName>Screen.kt
    ├── <FeatureName>ViewModel.kt
    ├── <FeatureName>State.kt
    ├── <FeatureName>Event.kt
    └── <FeatureName>Effect.kt       Optional
```

Do not create files or layer folders just for symmetry.

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
- Feature UI depends on provider/repository contracts through feature domain use cases.
- Provider DTOs must not leak into UI state.
- API keys and tokens must never be hardcoded or committed.
- Demo AI responses must be clearly named sample/demo/test data.
- Recipes AI calls must be isolated in `feature/recipes/data` datasources/repository implementations.

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
5. Validate using `ai/rules/validation-android.md`.
