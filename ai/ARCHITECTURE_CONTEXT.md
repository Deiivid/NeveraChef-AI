# NeveraChef AI — Architecture Context

> **Version:** 2026-05-26  
> **Scope:** Stable architecture context for NeveraChef AI.  
> **Audience:** AI agents, Codex tasks and developers working inside the repository.

Use this file when a task needs architecture, package ownership, source-set boundaries or dependency direction.

This is not a workflow file. For execution rules, use `AGENTS.md` first. For task-specific behaviour, use the relevant `.ai/skills/*/SKILL.md` file.

---

## Architecture intent

NeveraChef AI is an Android-first Kotlin Multiplatform / Compose Multiplatform app.

The architecture should stay simple, local-first and production-oriented. Prefer small, explicit, testable code over abstract layers created only to look clean.

Optimize for:

- clear feature ownership
- safe KMP boundaries
- Compose-first UI
- local-first data ownership
- predictable state management
- minimal dependencies
- reviewable diffs

Do not optimize for architecture theatre.

---

## Architecture decisions

General stack and root agent rules live in `AGENTS.md`. This file documents architecture-specific decisions only.

| Area | Architecture decision |
|---|---|
| App shape | Android-first Kotlin Multiplatform / Compose Multiplatform app |
| Shared module | `shared/` owns common product logic and shared Compose where practical |
| Hosts | `androidApp/` and `iosApp/` own platform host integration |
| Architecture style | Pragmatic Clean Architecture with MVVM/MVI-style state when useful |
| Persistence | Local-first for inventory, pantry, recipes and shopping data when persistence is required |
| AI integration | Provider/repository abstraction. UI must not call AI providers directly |

---

## Related `.ai/` files

The high-level repository map lives in `AGENTS.md`. This file should not duplicate it.

| File | Purpose |
|---|---|
| `.ai/ARCHITECTURE_CONTEXT.md` | Stable architecture and source ownership. This file. |
| `.ai/PRODUCT_CONTEXT.md` | Product scope, users, screens and MVP decisions. |
| `.ai/VALIDATION_ANDROID.md` | Android/KMP build, test, lint, static analysis and visual validation rules. |
| `.ai/WORKFLOW_FEATURE.md` | Active feature progress, decisions and pending work. |
| `.ai/skills/*/SKILL.md` | Task-specific execution rules. Load only when relevant. |

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
    │       │   ├── model/
    │       │   ├── repository/
    │       │   └── usecase/
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

Rules:

- Do not create empty folders just to match this shape.
- Add `domain/repository` or `domain/usecase` only when abstraction/business logic is actually needed.
- Keep feature code grouped by product area.
- Keep platform-specific code out of `commonMain`.

---

## Source-set ownership

| Source set / module | Owns | Must not own |
|---|---|---|
| `shared/commonMain` | Shared Kotlin, shared Compose UI, domain models, state, events, common interfaces | Android framework APIs, iOS framework APIs, JVM-only APIs, platform-specific storage details |
| `shared/androidMain` | Android-specific implementations for shared contracts | Shared product logic that could live in `commonMain` |
| `shared/iosMain` | iOS-specific implementations for shared contracts | Shared product logic that could live in `commonMain` |
| `androidApp/` | Android host, Android framework integration, activity/app wiring | Shared business rules or reusable feature internals |
| `iosApp/` | iOS host, Swift/iOS integration | Shared business rules or reusable feature internals |

KMP boundary rules live here. `AGENTS.md` only keeps the non-negotiable summary.

Rules:

- Put common behaviour in `commonMain` only when it works for all supported targets.
- Put platform behaviour in platform source sets or host apps.
- Use `expect/actual` only for real platform differences requiring a shared contract.
- Do not move code to shared just because KMP allows it.
- Do not import Android/iOS framework APIs from `shared/commonMain`.

---

## Package responsibilities

| Package | Responsibility | Rules |
|---|---|---|
| `app/` | Shared app composition root | Keep thin. Connect theme, navigation and top-level state only. |
| `core/designsystem/` | Theme, colors, typography, spacing, shapes and design primitives | No feature dependency. No business rules. |
| `core/ui/` | Reusable shared UI components and Compose helpers | No persistence. No feature-specific business logic. |
| `core/persistence/` | Local storage APIs and implementations | Do not access directly from Composables. Hide storage details behind repository/data APIs. |
| `core/preferences/` | User settings and preferences storage | Access through clear APIs. Do not leak storage details to UI. |
| `domain/model/` | Pure business/domain models | No UI, no persistence implementation, no platform APIs. |
| `domain/usecase/` | Business actions, validation and orchestration when useful | Pure Kotlin. Independently testable. Do not create use cases for one-line pass-throughs unless consistency requires it. |
| `domain/repository/` | Repository contracts | Contracts only. Implementations live outside domain. |
| `feature/*/` | Feature UI, screen state, events and presentation logic | May depend on domain contracts/models and core UI/design system. Must not depend on other feature internals. |
| `feature/navigation/` | Shared routes and navigation graph when applicable | Pass stable IDs/simple args. Do not pass large mutable objects. |

---

## Dependency direction

Preferred flow:

```text
Screen -> Event -> ViewModel/StateHolder -> UseCase -> Repository contract -> Data source
Screen <- State <- ViewModel/StateHolder <- Result  <- Repository          <- Data source
```

Allowed dependencies:

```text
app -> feature
app -> core.designsystem
app -> core.ui

feature -> domain.model
feature -> domain.usecase
feature -> domain.repository contracts
feature -> core.ui
feature -> core.designsystem

core.persistence -> domain models/contracts when needed
core.preferences -> domain models/contracts when needed

platform source sets -> shared contracts
```

Forbidden dependencies:

```text
Screen -> persistence directly
Screen -> preferences directly for business decisions
Screen -> AI provider directly
Domain -> Compose
Domain -> Android framework
Domain -> iOS framework
Feature A -> Feature B internals
shared/commonMain -> android.*
shared/commonMain -> UIKit / Swift-only APIs
shared/commonMain -> JVM-only APIs unless the target set explicitly allows it
```

---

## Feature architecture

Use this structure for new or meaningfully reworked features when it improves clarity:

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

Do not create `Route`, `State`, `Event`, `Effect` or `ViewModel` files if the screen is too small to justify them.

---

## File responsibilities

| File | Responsibility | Avoid |
|---|---|---|
| `Route` | Wires navigation, state holder/ViewModel and screen | UI styling, business rules, persistence details |
| `Screen` | Stateless or mostly stateless Composable that renders state and emits events | Direct persistence, direct network/AI calls, complex business rules |
| `State` | Immutable render-ready UI state | Mutable collections, database entities, platform objects |
| `Event` | User intent from the UI | Implementation details or persistence-specific commands |
| `Effect` | One-off effects such as navigation, snackbar or toast | Long-lived state or business data |
| `ViewModel` / state holder | Produces state, handles events and coordinates use cases/repositories | UI styling, raw database implementation details |
| `UseCase` | Encapsulates business action or validation when it has real value | One-line pass-throughs without purpose |
| `Repository contract` | Defines data operations required by domain/presentation | Storage implementation details |
| `Repository implementation` | Coordinates data sources, mapping and persistence | Compose/UI concerns |

---

## Compose and state rules

Detailed Compose implementation rules should live in `.ai/skills/compose-screen-review/SKILL.md` when they become task-specific.

Baseline rules:

- Composables render state and emit events.
- Keep business decisions outside Composables.
- Keep persistence, preferences, network and AI provider calls outside Composables.
- UI state must be immutable and render-ready.
- Loading, empty and error states should be explicit when meaningful.
- Use events for user intent, not direct mutation from UI.
- Use effects only for one-off actions such as navigation, snackbar, toast or permission prompts.
- Handle UI effects through a lifecycle-aware collection of a side-effect flow, such as `SharedFlow` or `Channel`, to reduce event loss during configuration changes or screen recreation.
- Keep reusable UI in `core/ui/` only when reuse is real.
- Keep design tokens and primitives in `core/designsystem/`.

Example shape:

```kotlin
data class PantryUiState(
    val isLoading: Boolean = false,
    val items: List<PantryItemUi> = emptyList(),
    val errorMessage: String? = null,
)

sealed interface PantryEvent {
    data object AddItemClicked : PantryEvent
    data class ItemNameChanged(val value: String) : PantryEvent
    data object SaveClicked : PantryEvent
}
```

---

## Domain rules

Domain code should be pure Kotlin and independently testable.

Allowed in domain:

- business models
- validation rules
- use cases when they add clarity
- repository contracts
- value objects

Forbidden in domain:

- Compose APIs
- Android APIs
- iOS APIs
- persistence implementation details
- UI-specific state wording unless deliberately shared
- framework-specific exceptions when avoidable

---

## Persistence rules

NeveraChef AI should be local-first for inventory, pantry, recipes and shopping data when persistence is required.

Rules:

- Database/local storage is the source of truth for persisted inventory and shopping list data.
- UI state is not long-term storage.
- Keep persistence decisions in repository/data/persistence layers.
- Do not cache persisted business data in Composables.
- Keep migrations explicit and safe.
- Mention migration/data-loss risk when changing schemas.
- Do not introduce remote sync, accounts, cloud backup or backend persistence unless explicitly requested.

---

## AI integration rules

AI functionality must be isolated behind contracts.

Rules:

- UI must not call OpenAI, Gemini, Claude or any provider SDK/API directly.
- Feature code should depend on a provider/repository contract, not on concrete provider clients.
- Provider-specific request/response DTOs must not leak into UI state.
- API keys and tokens must never be hardcoded or committed.
- Mock/demo AI responses must be clearly named as sample/demo/test data.
- If adding AI persistence, clarify what is stored locally and whether any data leaves the device.

Preferred shape:

```text
Screen -> Event -> ViewModel/StateHolder -> AiRepository contract -> Provider implementation
Screen <- State <- ViewModel/StateHolder <- Domain/result model <- mapped provider response
```

---

## Navigation rules

- Keep routes stable and explicit.
- Prefer simple route arguments such as IDs, enums or primitive values.
- Do not pass large mutable objects between screens.
- Keep navigation side effects outside `Screen` where practical.
- Use one-off effects for navigation when driven by state holder logic.
- Keep navigation graph ownership in `app/` or `feature/navigation/` depending on current project structure.

---

## Testing strategy

Testing rules may move to a dedicated `.ai/TESTING_STRATEGY.md` if the project grows. Until then, use this baseline.

| Target | What to test | Preferred style |
|---|---|---|
| Domain models/value objects | Validation, invariants and edge cases | Pure Kotlin unit tests |
| Use cases | Business decisions and orchestration | Unit tests with fake repositories |
| Mappers | Persistence/provider/domain/UI conversions | Input-output unit tests |
| State holders/ViewModels | Event handling and emitted state | Coroutine/Flow unit tests |
| Repositories | Local-first behaviour and data mapping | Unit tests or integration tests depending on storage |
| Navigation effects | Only when logic exists | State/effect assertions |

Rules:

- Prefer behaviour tests over structure tests.
- Do not add heavy UI test infrastructure unless explicitly requested or already present.
- Do not mock everything by default. Use small fakes when they make tests clearer.
- Test business rules before visual details.
- Add regression tests for fixed bugs when the bug is logic-related.

---

## Platform decision matrix

| Need | Put it in |
|---|---|
| Pure model or validation | `shared/commonMain/domain` |
| Shared screen state/events | `shared/commonMain/feature/<feature>` |
| Shared Compose screen | `shared/commonMain/feature/<feature>` |
| Reusable design primitive | `shared/commonMain/core/designsystem` |
| Reusable UI component | `shared/commonMain/core/ui` |
| Local storage contract | `shared/commonMain/domain/repository` or `core/persistence` contract |
| Android-specific storage implementation | `shared/androidMain` or `androidApp/` depending on ownership |
| iOS-specific storage implementation | `shared/iosMain` or `iosApp/` depending on ownership |
| Android permission/API integration | `shared/androidMain` if behind shared contract, otherwise `androidApp/` |
| iOS permission/API integration | `shared/iosMain` if behind shared contract, otherwise `iosApp/` |
| App startup / activity wiring | `androidApp/` |
| Swift host wiring | `iosApp/` |

---

## Project-specific anti-patterns

| Bad | Good |
|---|---|
| Calling an AI provider directly from a Composable | Use an AI repository/provider contract outside UI. |
| Adding Android imports in `shared/commonMain` | Move the implementation to `shared/androidMain` or `androidApp/`. |
| Adding iOS/Swift-only APIs in `shared/commonMain` | Move the implementation to `shared/iosMain` or `iosApp/`. |
| Creating hardcoded fake data that looks production-ready | Use explicit preview/sample/demo/test naming. |
| Adding a DI framework because constructor injection feels repetitive | Keep constructor injection unless a framework is already present or explicitly requested. |
| Creating `UseCase` files for every getter automatically | Add use cases only when they hold business behaviour or improve consistency. |
| Moving all UI components to `core/ui` immediately | Keep components inside the feature until reuse is real. |
| Passing database entities directly into screen state | Map to render-ready UI models/state. |
| Reformatting unrelated files during architecture changes | Keep the diff scoped and reviewable. |

---

## Architecture change protocol for agents

Before editing architecture-sensitive code:

1. Read the user request.
2. Inspect the closest existing feature/package.
3. Inspect related state, events and route/screen files.
4. Inspect domain models/use cases only if business rules are involved.
5. Inspect persistence/preferences only if data access is involved.
6. Inspect platform source sets only if platform APIs are involved.
7. Classify the change as UI-only, state, domain, persistence, platform or cross-cutting.
8. Make the smallest safe change.

Before finishing:

1. Check imports for platform leaks.
2. Check business logic is not inside Composables.
3. Check persistence/preferences/AI providers are not called directly from Screens.
4. Check UI state is immutable.
5. Check feature-to-feature internals are not coupled.
6. Run the smallest useful validation command.
7. If validation is not run, say so clearly.

---

## Validation references

Validation command ownership lives in `.ai/VALIDATION_ANDROID.md`.

Architecture-sensitive changes should use that file to choose the smallest useful check for the touched area. This file must not duplicate Gradle commands or validation procedures.