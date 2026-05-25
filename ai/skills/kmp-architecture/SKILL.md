# KMP Architecture Skill

Use this skill when creating, reviewing or evolving architecture inside the NeveraChef AI Kotlin Multiplatform / Compose Multiplatform project.

This skill is based on Google's modern Android architecture guidance adapted to KMP, plus JetBrains/Kotlin guidance for Compose Multiplatform source-set boundaries.

---

## Sources of truth

| Source | Use for |
|---|---|
| Google Android architecture | Layering, UI state, UDF, ViewModel/state holder, data/domain boundaries. |
| Google UI layer guidance | UI renders state and sends events; state holders own UI logic. |
| Google Compose guidance | State hoisting, composable responsibilities, stable state. |
| Google KMP ViewModel docs | ViewModel in common code and platform restrictions. |
| JetBrains/Kotlin Compose Multiplatform docs | Common UI, shared source sets, platform-specific source sets. |
| Repository code | Final authority for existing project conventions. |

---

## Architecture baseline

| Layer | Responsibility |
|---|---|
| App | App composition root, theme and navigation setup. |
| Feature | Screen UI, route wiring, state/events/effects and presentation logic. |
| Domain | Pure models, use cases and repository contracts when useful. |
| Core UI/design system | Shared visual primitives, theme, tokens and reusable UI components. |
| Persistence/preferences | Local-first storage and user settings behind clear APIs. |
| Platform source sets | Android/iOS-specific implementations only. |

---

## Recommended package shape

For new or reworked features:

```text
feature/<featureName>/
├── <FeatureName>Route.kt
├── <FeatureName>Screen.kt
├── <FeatureName>State.kt
├── <FeatureName>Event.kt
├── <FeatureName>Effect.kt        Optional.
└── <FeatureName>ViewModel.kt     Only when needed.
```

Use this shape only when it improves clarity. Do not create empty architecture theatre.

---

## File responsibilities

| File | Responsibility | Rule |
|---|---|---|
| `Route` | Connect navigation, DI/state holder and screen. | Keep thin. |
| `Screen` | Render immutable state and emit events. | No persistence or business logic. |
| `State` | Describe everything the UI needs to render. | Immutable and render-ready. |
| `Event` | Represent user intent. | No framework/persistence types. |
| `Effect` | One-off effects such as navigation/snackbar. | Optional; never for normal rendering state. |
| `ViewModel` | Produce state and handle events. | Calls use cases/repository contracts. |

---

## Dependency flow

Preferred:

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

- Put common behavior in `commonMain` when it truly works for all targets.
- Put platform behavior in platform source sets.
- Use `expect/actual` only for real platform differences that need a shared contract.
- Do not move code to shared just because KMP allows it.
- Do not import Android/iOS framework APIs from `shared/commonMain`.

---

## State rules

- UI state must be immutable.
- State should contain render-ready values.
- Loading, empty and error states must be explicit when possible.
- Avoid mutable collections in state.
- Avoid persistence/database entities in UI state when they expose storage details.
- Prefer sealed state when the screen has strongly distinct states.

---

## ViewModel/state holder rules

Use a ViewModel/state holder when a screen has:

- async work
- persistence/preferences access
- validation
- multiple user events
- non-trivial state transformations
- logic that should survive recomposition

Rules:

- ViewModel/state holder produces state and handles events.
- Common ViewModels must not depend on Android `Context`, `Application`, Activity, Fragment, UIKit or Swift-only types.
- If platform access is needed, create a shared contract and implement it in platform source sets.

---

## Domain rules

Add `domain/usecase/` only when:

- business logic is reused
- validation is non-trivial
- behavior needs independent tests
- the ViewModel is becoming a transaction script

Add `domain/repository/` only when:

- feature logic needs data without knowing persistence details
- data may come from persistence/preferences/network
- tests need fake implementations

Domain must be pure Kotlin and platform independent.

---

## Codex execution protocol

Before changing architecture:

1. Inspect the existing feature package.
2. Inspect related domain models.
3. Inspect persistence/preferences only if data access is involved.
4. Classify the task scope.
5. Propose the smallest safe architecture change.

Before finishing:

1. Check imports for platform leaks.
2. Check business logic is not inside Composables.
3. Check persistence is not called directly from Screens.
4. Check UI state is immutable.
5. Run the smallest useful validation command.
6. If validation is not run, say so clearly.

---

## Final output

Return only:

```text
Files changed:
- ...

Validation:
- ...

Notes:
- ...
```
