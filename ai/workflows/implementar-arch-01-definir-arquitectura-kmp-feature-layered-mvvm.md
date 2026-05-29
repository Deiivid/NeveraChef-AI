# Workflow - Implement ARCH-01 KMP Feature-Layered Architecture

## Source

- Notion: `ARCH-01 Define KMP feature-layered MVVM architecture`
- Initial Notion status: `Not started`
- Workflow: `Preparation`
- Expected validation: documentation review and structure verification if files are created.

## Objective

Define and implement the documentation baseline for evolving NeveraChef AI into a KMP feature-layered architecture with independent features and strict local layer rules.

This workflow must not move product code unless the user explicitly asks for it.

## Acceptance Criteria

- Architecture document created or updated.
- Proposed folder structure for `shared`, `androidApp`, and `iosApp`.
- Clear boundaries between `data`, `domain`, and `ui`.
- Strict dependency rule: `ui -> domain <- data`.
- Per-feature domain; no global application `domain` layer.
- Local storage behind `data` datasources.
- `pantry` and `shopping` explicitly implemented as local-first `data + domain + ui`.
- `recipes` explicitly prepared for AI with domain contracts and provider-facing `data`.
- Complete feature example.
- Strategy for root, module, and feature-level `AGENTS.md` files.
- Compatible with Kotlin Multiplatform, Compose Multiplatform, and MVVM.
- No dependency, Gradle, persistence, CI/CD, or public API changes.

## Required Inputs

Before running this workflow, review only:

- `AGENTS.md`
- `ai/ARCHITECTURE_CONTEXT.md`
- `ai/VALIDATION_ANDROID.md`
- Notion page `ARCH-01 Define KMP feature-layered MVVM architecture`
- Current structure under:
  - `shared/src/commonMain/kotlin`
  - `shared/src/androidMain/kotlin`
  - `shared/src/iosMain/kotlin`
  - `androidApp/src/main/kotlin`
  - `iosApp/iosApp`

Do not scan the whole repository by default.

## Deliverables

Create or update:

- `ai/architecture/ARCH-01-kmp-feature-layered-mvvm.md`

Do not create empty production feature folders during this task.

## Target Architecture

```text
shared/src/commonMain/kotlin/es/neverachefai/
  app/
  core/
    designsystem/
    persistence/
    preferences/
    ui/
  feature/
    home/
      data/
      domain/
      ui/
    pantry/
      data/
      domain/
      ui/
    recipes/
      data/
      domain/
      ui/
    shopping/
      data/
      domain/
      ui/
    settings/
      data/
      domain/
      ui/
    onboarding/
      data/
      domain/
      ui/
    navigation/
```

## Layer Rules

### `ui`

- Contains screens, ViewModels, state, events and effects.
- Depends on feature `domain`.
- Must not depend on feature `data`.
- Must not call persistence, preferences, network, or AI providers directly.

### `domain`

- Pure Kotlin.
- Per-feature only.
- Contains models, use cases, validation, errors and repository interfaces.
- Must not depend on `ui`, `data`, Compose, Android, iOS, persistence, network or provider SDKs.
- There is no global application `shared/domain` layer.

### `data`

- Implements repository interfaces from `domain`.
- Contains local datasources, remote datasources if needed, DTOs, mappers and persistence models.
- Depends on feature `domain`.
- Must not depend on feature `ui`.
- Local data is stored through datasources in this layer.
- For `recipes`, AI provider integration belongs in this layer.

### `core`

- Infrastructure and shared UI foundation only.
- No feature-specific business logic.
- No feature-specific models.
- Features depend on `core`; `core` never depends on features.

## Dependency Rules

Allowed:

```text
app -> feature modules
feature/<name>/ui -> feature/<name>/domain
feature/<name>/data -> feature/<name>/domain
feature/<name>/ui -> core/designsystem
feature/<name>/ui -> core/ui
feature/<name>/data -> core/persistence
feature/<name>/data -> core/preferences
platform source sets -> shared contracts
```

Forbidden:

```text
feature/<name>/ui -> feature/<name>/data
feature/<name>/data -> feature/<name>/ui
feature A -> feature B internals
shared/commonMain -> android.* / UIKit / Swift-only APIs
domain -> Compose
domain -> Android/iOS framework
```

## Feature Example

```text
feature/pantry/
  PantryModule.kt
  data/
    PantryRepositoryImpl.kt
    PantryLocalDataSource.kt
    dto/
    mapper/
  domain/
    PantryRepository.kt
    model/
      PantryItem.kt
    usecase/
      GetPantryItemsUseCase.kt
  ui/
    PantryScreen.kt
    PantryViewModel.kt
    PantryState.kt
    PantryEvent.kt
```

Rule: create only files that add real behavior. Do not create classes for symmetry.

## `AGENTS.md` Strategy

### Root

Defines global rules:

- Stack.
- Architecture.
- Validation.
- Security.
- Expected output.
- Change boundaries.

### Feature

Each `feature/<name>/AGENTS.md` must define:

- Feature responsibility.
- Allowed layers.
- Allowed dependencies.
- Models owned by the feature.
- Areas the feature must not touch.
- Minimum validation command.
- Local Definition of Done.

Create feature-level agent files only when the feature has meaningful local rules.

## Execution Phases

### 1. Minimal Context

- Read the ARCH-01 Notion task.
- Read `AGENTS.md`.
- Read `ai/ARCHITECTURE_CONTEXT.md`.
- Read `ai/VALIDATION_ANDROID.md`.
- Inspect the current folder structure without loading irrelevant code.

Expected internal output:

- Current state.
- Gaps against the target architecture.
- Required documentation files.

### 2. Architecture Document

Create `ai/architecture/ARCH-01-kmp-feature-layered-mvvm.md` with:

- Objective.
- Project structure.
- Strict layer rules.
- Dependency rules.
- Local-first data rule.
- Feature example.
- KMP rules.
- Naming conventions.
- Incremental migration notes.
- Validation.

### 3. Incremental Migration Plan

The document must propose this migration sequence:

1. Freeze architecture rules in documentation.
2. Keep the current `feature/` root.
3. Remove reliance on any global `domain/` package over time.
4. Migrate one feature at a time.
5. Establish feature `domain` first.
6. Move persistence behind feature `data` datasources.
7. Move screens and ViewModels into feature `ui`.
8. Keep hosts as integration layers.
9. Run validation after each small migration.

Feature priority in this workflow:

1. `pantry`: enforce local-first through `data` datasources and repository implementation.
2. `shopping`: enforce local-first through `data` datasources and repository implementation.
3. `recipes`: keep `ui -> domain` usage and isolate AI provider calls in `data`.

### 4. Scope Control

Do not do this in ARCH-01:

- Move real screens.
- Change Kotlin packages.
- Create Gradle modules.
- Add dependencies.
- Add persistence.
- Create fake production implementations.
- Change navigation.
- Change CI/CD.

If the user asks for physical folders in source code, creating structure with `.gitkeep` is forbidden unless explicitly approved.

### 5. Validation

For documentation-only work:

```text
Not run: documentation-only change.
```

If files are created under source code:

```bash
./gradlew :shared:compileAndroidMain
```

If `androidApp` is touched:

```bash
./gradlew :androidApp:assembleDebug
```

## Execution Prompt

Use this prompt to run ARCH-01:

```text
Execute ARCH-01 from ai/workflows/implementar-arch-01-definir-arquitectura-kmp-feature-layered-mvvm.md.
Review Notion ARCH-01.
Respect AGENTS.md.
Create the KMP feature-layered architecture documentation for NeveraChef AI.
Use ui -> domain <- data.
Do not move product code.
Do not add dependencies.
Do not create empty folders in source code.
Return changed files, validation, and risk.
```

## Expected Final Output

```text
Files:
- ai/architecture/ARCH-01-kmp-feature-layered-mvvm.md

Done:
- ARCH-01 architecture documentation created.

Check:
- Not run: documentation-only change.

Risk:
- none
```
