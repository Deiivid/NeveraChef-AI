---
name: kmp-architecture
description: Review or evolve NeveraChef AI Android/Kotlin/KMP architecture. Use when checking feature-layered MVVM compliance, package boundaries, presentation-domain-data direction, Compose file scalability, ViewModel/state/event placement, repository/use case placement, platform leaks, or whether a feature should be split into smaller files.
---

# KMP Architecture

Use this skill for architecture reviews and architecture-sensitive changes in NeveraChef AI.

## Load Order

1. Inspect the direct feature/package involved.
2. Open `ai/context/architecture.md` only when architecture rules are needed.
3. Open `ai/context/product.md` only when product semantics are touched.
4. Open `ai/rules/validation-android.md` only when choosing validation.

Do not scan the whole repository by default.

## Target Architecture

Default direction:

```text
presentation -> domain <- data
```

Existing features may still use `ui/`; treat `ui/` as `presentation`. Do not create both names inside the same feature unless explicitly migrating.

MVVM flow:

```text
Screen -> Event -> ViewModel/StateHolder -> UseCase -> Repository contract <- Repository implementation <- Data source
Screen <- UiState <- ViewModel/StateHolder
```

## Layer Rules

- `presentation` / `ui`: screens, composables, ViewModels/state holders, UI state, events and effects.
- `domain`: pure Kotlin models, validation, useful use cases and repository contracts.
- `data`: repository implementations, datasources, DTOs and mappers.
- `app`: composition root, app state and navigation wiring.
- `core/designsystem`: tokens, theme and design primitives.
- `core/ui`: genuinely reusable UI components only.
- `core/persistence` and `core/preferences`: storage APIs and implementations behind contracts.

## Forbidden

```text
presentation/ui -> data
data -> presentation/ui
domain -> Compose
domain -> Android/iOS framework APIs
Screen -> persistence/preferences/AI provider directly
Feature A -> Feature B internals
shared/commonMain -> android.* / UIKit / Swift-only APIs
```

## Compose Scalability

- Aim for files under 400-500 lines.
- Split large screens by responsibility:
  - `sections/` for major screen areas.
  - `components/` for repeated or reusable UI pieces.
  - `model/` or state files for UI models when needed.
- Keep Composables render-focused: state in, events out.
- Move formatting and mapping out of large composables when it grows.
- Keep preview/sample data separate from production state.
- Move to `core/ui` only after real reuse across features.

## Review Checklist

Before editing or reporting:

1. Identify the feature and current package shape.
2. Check dependency direction against `presentation -> domain <- data`.
3. Check ViewModel/state/event ownership.
4. Check Composables do not own persistence, provider calls or business rules.
5. Check file size and split candidates.
6. Check platform APIs stay out of `shared/commonMain`.
7. Check validation command from `ai/rules/validation-android.md` if code changed.

## Output

For reviews, report only findings that matter:

```text
Findings:
- severity - file - issue

Suggested changes:
- ...

Validation:
- command or Not run
```

For implementations, use the repo final format from `AGENTS.md`.
