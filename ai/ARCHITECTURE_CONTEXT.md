# NeveraChef AI — Architecture Context

Use this file when an AI agent, Codex task or developer needs stable architecture context for NeveraChef AI.

This file explains how the project should be structured and where code should live. It is not a task workflow. For task-specific workflows, use `.ai/skills/*/SKILL.md`.

---

## Project overview

| Area | Definition |
|---|---|
| Project | NeveraChef AI |
| App type | Kotlin Multiplatform / Compose Multiplatform app |
| Android host | `androidApp/` |
| iOS host | `iosApp/` |
| Shared code | `shared/` |
| UI approach | Compose Multiplatform / shared Compose where practical |
| Architecture style | Pragmatic Clean Architecture with MVVM/MVI-style state management when useful |
| Persistence style | Local-first when data must survive app restarts |

---

## Repository structure

```text
NeveraChefAI/
├── AGENTS.md
├── README.md
├── CONTRIBUTING.md
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── local.properties
├── gradle/
├── .ai/
│   ├── ARCHITECTURE_CONTEXT.md
│   ├── PRODUCT_CONTEXT.md
│   ├── VALIDATION_COMMANDS.md
│   ├── PR_REVIEW_CHECKLIST.md
│   ├── TASK_TEMPLATE.md
│   ├── WORKFLOW_FEATURE.md
│   └── skills/
├── prompts/
├── androidApp/
├── iosApp/
└── shared/
```

---

## Shared module structure

Expected shared module shape:

```text
shared/
└── src/
    ├── commonMain/
    │   ├── composeResources/
    │   └── kotlin/es/neverachefai/
    │       ├── app/
    │       ├── core/
    │       │   ├── designsystem/
    │       │   ├── persistence/
    │       │   ├── preferences/
    │       │   └── ui/
    │       ├── domain/
    │       │   └── model/
    │       └── feature/
    │           ├── home/
    │           ├── navigation/
    │           ├── onboarding/
    │           ├── pantry/
    │           ├── recipes/
    │           ├── settings/
    │           └── shopping/
    ├── commonTest/
    ├── androidMain/
    ├── androidHostTest/
    ├── iosMain/
    └── iosTest/
```

---

## Package responsibilities

| Package | Responsibility | Rules |
|---|---|---|
| `app/` | Shared app composition root. | Keep thin. Connect theme, navigation and top-level state only. |
| `core/designsystem/` | Theme, colors, typography, spacing, shapes and design primitives. | No feature or domain dependency. |
| `core/ui/` | Reusable shared UI components and helpers. | No business rules. No persistence. |
| `core/persistence/` | Local-first storage APIs and implementations. | Do not access directly from Composables. |
| `core/preferences/` | User settings and preferences storage. | Access through clear APIs. |
| `domain/model/` | Pure business/domain models. | No UI, no persistence details, no platform APIs. |
| `domain/usecase/` | Business actions and validation logic. Add only when useful. | Pure Kotlin. Independently testable. |
| `domain/repository/` | Repository contracts. Add only when data access needs abstraction. | Contracts only. |
| `feature/*/` | Feature UI, screen state, events and presentation logic. | May use domain and core UI/design system. |
| `feature/navigation/` | Shared routes and navigation graph when applicable. | Pass stable IDs/simple args, not large mutable objects. |

---

## Architecture baseline

Use Clean Architecture pragmatically.

| Principle | Rule |
|---|---|
| UI | Renders state and emits events. No business rules. |
| Presentation | Holds UI state, handles user events and calls use cases/repositories. |
| Domain | Contains pure models, business rules, use cases and repository contracts when useful. |
| Data/persistence | Owns storage details, mapping and local-first behaviour. |
| Platform | Android/iOS APIs stay in platform source sets or host apps. |
| Shared | `commonMain` must remain platform agnostic. |

---

## Recommended feature structure

Use this shape for new or reworked features when it improves clarity:

```text
feature/<featureName>/
├── <FeatureName>Route.kt
├── <FeatureName>Screen.kt
├── <FeatureName>State.kt
├── <FeatureName>Event.kt
├── <FeatureName>Effect.kt        Optional
└── <FeatureName>ViewModel.kt     Only when needed
```

Example:

```text
feature/pantry/
├── PantryRoute.kt
├── PantryScreen.kt
├── PantryState.kt
├── PantryEvent.kt
├── PantryEffect.kt
└── PantryViewModel.kt
```

Do not create empty architecture folders just to look clean.

---

## File responsibilities

| File | Responsibility |
|---|---|
| `Route` | Wires navigation, state holder/ViewModel and screen. |
| `Screen` | Stateless or mostly stateless Composable that renders state and emits events. |
| `State` | Immutable render-ready UI state. |
| `Event` | User intent from the UI. |
| `Effect` | One-off effects such as navigation, snackbar or toast. Use only when needed. |
| `ViewModel` / state holder | Produces state, handles events and coordinates use cases/repositories. |

---

## Dependency flow

Preferred flow:

```text
Screen -> Event -> ViewModel/StateHolder -> UseCase -> Repository contract -> Data source
Screen <- State <- ViewModel/StateHolder <- Result  <- Repository          <- Data source
```

Allowed:

```text
app -> feature
feature -> domain
feature -> core.ui
feature -> core.designsystem
feature -> domain.repository contracts
core.persistence -> domain models/contracts when needed
core.preferences -> domain models/contracts when needed
```

Forbidden:

```text
Screen -> persistence directly
Screen -> preferences directly for business decisions
Domain -> Compose
Domain -> Android framework
Domain -> iOS framework
Feature A -> Feature B internals
shared/commonMain -> android.*
shared/commonMain -> UIKit / Swift-only APIs
```

---

## KMP boundaries

| Source set / module | Responsibility |
|---|---|
| `shared/commonMain` | Shared Kotlin and shared Compose code. No platform APIs. |
| `shared/androidMain` | Android-specific implementations for shared contracts. |
| `shared/iosMain` | iOS-specific implementations for shared contracts. |
| `androidApp/` | Android app host and Android framework integration. |
| `iosApp/` | iOS app host and Swift/iOS integration. |

Rules:

- Put common behaviour in `commonMain` only when it truly works for all targets.
- Put platform behaviour in platform source sets.
- Use `expect/actual` only for real platform differences that need a shared contract.
- Do not move code to shared just because KMP allows it.
- Do not import Android/iOS framework APIs from `shared/commonMain`.

---

## State management rules

- UI state must be immutable.
- State should contain render-ready values.
- Loading, empty and error states should be explicit when possible.
- Avoid mutable collections in state.
- Avoid persistence/database entities in UI state if they expose storage details.
- Prefer sealed state when the screen has strongly distinct states.

---

## Persistence rules

NeveraChef AI should be local-first for inventory and shopping data.

Rules:

- Database/local storage is the source of truth for persisted inventory and shopping list data.
- UI state is not long-term storage.
- Keep persistence decisions in repository/data/persistence layers.
- Do not cache in Composables.
- Keep migrations explicit and safe.
- Mention migration/data-loss risk when changing schemas.

---

## Agent rules

Before architecture changes:

1. Inspect the existing feature/package.
2. Inspect related domain models.
3. Inspect persistence/preferences only if data access is involved.
4. Classify the change scope.
5. Make the smallest safe architecture change.

Before finishing:

1. Check imports for platform leaks.
2. Check business logic is not inside Composables.
3. Check persistence is not called directly from Screens.
4. Check UI state is immutable.
5. Run the smallest useful validation command.
6. If validation is not run, say so clearly.
