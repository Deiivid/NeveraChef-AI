# PROJECT_CONTEXT

Last updated: 2026-05-15

## 1. What NeveraChef-AI is

NeveraChef-AI is a mobile product to help users decide what to cook with ingredients they already have at home, with AI support for recipe generation and shopping list extraction.

## 2. Problem it solves

- Users do not know what to cook with available ingredients.
- Food waste due to poor planning.
- Repetitive meals and disorganized shopping.

## 3. Current repository state

The repository is not empty. It currently contains a Kotlin Multiplatform scaffold:

- `androidApp/` Android host app
- `shared/` KMP module with Compose Multiplatform UI entry (`App()`)
- `iosApp/` SwiftUI wrapper hosting shared Compose UI via `MainViewController`

## 4. Source-of-truth classification

## Confirmed by code

- Project currently uses KMP + Compose Multiplatform in `shared`.
- Android and iOS both render `shared` Compose UI (`App()`).
- A first functional placeholder flow is implemented in shared UI:
  - Welcome
  - Onboarding
  - Preferences
  - Home
  - Pantry
  - Add ingredients
  - Ingredient review
  - Recipe generation
  - Recipe results
  - Recipe detail
  - Shopping list
  - Settings
- The flow compiles for Android (`:androidApp:assembleDebug`).
- The codebase is now split by architecture folders inside `shared` (`app/core/domain/feature`).

## Confirmed by Markdown docs in repo

- Product direction, MVP goals, architecture rationale, and initialization plans are documented in:
  - `README.md`
  - `docs/architecture/neverachef-architecture-rationale.md`
  - `docs/plans/*`

## Confirmed by Notion

- Android-first MVP.
- Local-first, no login in MVP.
- AI text integration after mock app foundations.
- Sprint roadmap 0 -> 6.
- Core flows: onboarding, ingredients, review, recipes, recipe detail, shopping list, settings.

## Confirmed by Pencil

- Visual references for screens 01-12.
- Shared design language: color tokens, typography, component candidates.
- Main flow and bottom navigation intentions.

## Inferred

- Best initial UI strategy is shared Compose Multiplatform UI (already configured), with optional native divergence only if needed later.
- Keep MVP simple and avoid heavy infra early (no backend, no multimodal first).

## Pending confirmation

- Initial AI provider for Sprint 3 (OpenAI vs Gemini).
- Exact persistence approach milestone split (DataStore-only first vs Room in Sprint 4).
- Final scope of weekly menu inside MVP vs post-MVP.

## 5. MVP scope snapshot

MVP includes:

1. Manual ingredient input.
2. Basic preferences.
3. AI recipe generation by text.
4. Recipe results and recipe detail.
5. Shopping list from missing ingredients.

MVP excludes:

- Login
- Cloud sync
- Full backend platform
- Production iOS parity commitments beyond MVP scope

## 6. Current implementation maturity

- Product and architecture documentation: in progress, now centralized.
- UI implementation: placeholder functional shared flow, no business logic yet.
- AI integration: not implemented.
- Domain/data separation for product logic: not implemented.

## 7. Working rule for future sessions

Before coding:

1. Read this file.
2. Read `docs/ARCHITECTURE.md`.
3. Read `docs/UI_SCREENS.md`.
4. Read `docs/DESIGN_SYSTEM.md`.
5. Validate against latest Notion decisions.
