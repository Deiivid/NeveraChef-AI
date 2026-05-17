# ARCHITECTURE

Last updated: 2026-05-15

## 1. Chosen architecture strategy

Given the current codebase state, the recommended and selected strategy is:

- Kotlin Multiplatform shared logic.
- Compose Multiplatform shared UI as default for Android + iOS (already active).
- Keep iOS host app as SwiftUI wrapper for shared Compose UI.

Reason:

- This is the lowest-risk path aligned with current repo setup.
- It avoids duplicating UI logic prematurely.
- It still keeps room for native divergence when justified.

## 2. Why not split Android Compose + SwiftUI now

Not selected for MVP start because:

- Current scaffold already supports shared Compose screen on both platforms.
- Duplicating screens now would increase delivery risk and maintenance cost.
- Product priority is validating flow and AI integration first.

This remains an option later for platform-specific UX optimization.

## 3. Current module reality

Current modules:

- `:androidApp`
- `:shared`

This is confirmed by `settings.gradle.kts`.

## 4. Current package architecture inside `shared`

Implemented package split:

```text
shared/src/commonMain/kotlin/es/neverachefai/
  app/
    NeveraChefApp.kt
  core/
    designsystem/
    ui/components/
  domain/
    model/
  feature/
    navigation/
    onboarding/ui/
    home/ui/
    pantry/ui/
    recipes/ui/
    shopping/ui/
    settings/ui/
```

This delivers architectural separation now without forcing a full Gradle multi-module migration yet.

## 5. Target module evolution (incremental)

To avoid overengineering, use phased modularization:

Phase 1 (inside `shared` package boundaries):
- organize by package: `core`, `domain`, `data`, `feature`.

Phase 2 (optional Gradle split when needed):
- `core:*`, `domain:*`, `data:*`, `feature:*` modules.

## 6. Layer responsibilities

Presentation:
- Compose screens, UI state, UI events, navigation bindings.

Domain:
- Pure business models and use cases.
- No Android/iOS framework dependencies.

Data:
- Repositories, DTOs, mappers, persistence and remote datasource adapters.

Core:
- Shared primitives (result/error), design system tokens/components, common utilities.

## 7. Data flow

```text
UI Screen
 -> ViewModel/State holder
 -> UseCase
 -> Repository interface (domain)
 -> Repository implementation (data)
 -> Remote/Local datasource
 -> DTO -> Mapper -> Domain model
 -> UI state
```

## 8. Android strategy

- `androidApp` remains host entrypoint.
- It should mainly wire platform setup and invoke shared Compose app root.
- Android-specific integrations (permissions, camera, speech) should be behind interfaces and added only in their sprint.

## 9. iOS strategy

- `iosApp` remains SwiftUI host shell.
- It embeds `MainViewController()` from shared KMP module.
- iOS-only behavior should be introduced via platform adapters only when needed.

## 10. Navigation strategy (MVP)

- Onboarding flow.
- Main flow with bottom navigation:
  - Home
  - Pantry
  - Shopping
  - Settings
- Recipes and recipe detail routes as sub-flow.

## 11. Technical constraints

1. No backend dependency for MVP core flow.
2. Local-first data behavior.
3. AI responses must be parseable structured JSON.
4. Avoid introducing heavy build or DI complexity early.
