# Workflow - Implement ARCH-01 Define KMP Feature-Layered MVVM Architecture

## Source

- Notion: `ARCH-01 Define KMP feature-layered MVVM architecture`
- Initial Notion status: `Not started`
- Workflow: `Preparation`
- Expected validation: documentation review and structure verification if files are created.

## Objective

Define and implement the documentation and execution baseline for evolving NeveraChef AI into a KMP feature-layered MVVM architecture with independent features and clear folder-level rules.

This workflow must not move product code unless the user explicitly asks for it.

## Acceptance Criteria

- Architecture document created or updated.
- Proposed folder structure for `shared`, `androidApp`, and `iosApp`.
- Clear boundaries between `data`, `domain`, and `presentation`.
- Clear dependency rules.
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

Optional, only if useful for later execution:

- `ai/architecture/templates/AGENTS-root.md`
- `ai/architecture/templates/AGENTS-feature.md`
- `ai/architecture/templates/feature-structure.md`

Do not create empty production feature folders during this task.

## Target Architecture

```text
shared/
  src/
    commonMain/
      kotlin/
        es/neverachefai/
          app/
          core/
            domain/
            data/
            presentation/
            designsystem/
            ui/
          features/
            inventory/
              domain/
              data/
              presentation/
              AGENTS.md
            shopping/
              domain/
              data/
              presentation/
              AGENTS.md
            recipes/
              domain/
              data/
              presentation/
              AGENTS.md
            product/
              domain/
              data/
              presentation/
              AGENTS.md
            settings/
              domain/
              data/
              presentation/
              AGENTS.md
            localai/
              domain/
              data/
              presentation/
              AGENTS.md
    androidMain/
      kotlin/
        es/neverachefai/
          platform/
    iosMain/
      kotlin/
        es/neverachefai/
          platform/
androidApp/
iosApp/
AGENTS.md
```

## Layer Rules

### `domain`

- Pure Kotlin.
- Domain models, value objects, business rules, and useful use cases.
- Repository contracts when persistence, AI, network, or platform behavior sits behind them.
- Must not depend on Compose, Android, iOS, concrete storage, or external DTOs.

### `data`

- Implements repository contracts.
- Contains datasources, internal DTOs, mappers, and adapters.
- Can depend on `domain`.
- Must not expose persistence, network, or provider details to `presentation`.

### `presentation`

- Contains `Screen`, `Route`, `UiState`, `Event`, `Effect`, and ViewModel/state holder files when needed.
- Renders immutable state and emits events.
- Must not call persistence, preferences, network, or AI providers directly.
- Can depend on `domain`, `core/designsystem`, and `core/ui`.

### `core`

- Contains only genuinely shared code.
- Must not become a dumping ground for unclear ownership.
- `core/designsystem` and `core/ui` contain reusable UI primitives.
- `core/data`, `core/persistence`, or `core/preferences` must stay behind contracts.

### Hosts

- `androidApp` owns Android integration: Activity, manifest, permissions, wiring, and Android APIs.
- `iosApp` owns iOS integration: SwiftUI host, lifecycle, and iOS wiring.
- Product logic must not live in host modules.

## Dependency Rules

Allowed:

```text
app -> features
features/presentation -> features/domain
features/data -> features/domain
features/presentation -> core/designsystem
features/presentation -> core/ui
platform source sets -> shared contracts
```

Forbidden:

```text
domain -> Compose
domain -> Android/iOS APIs
presentation -> persistence/provider directly
feature A -> feature B internals
shared/commonMain -> android.* / UIKit / Swift-only APIs
```

## Feature Example

```text
features/inventory/
  AGENTS.md
  domain/
    InventoryItem.kt
    InventoryLocation.kt
    InventoryRepository.kt
    ObserveInventoryItemsUseCase.kt
  data/
    InventoryRepositoryImpl.kt
    InventoryLocalDataSource.kt
    InventoryItemMapper.kt
  presentation/
    InventoryRoute.kt
    InventoryScreen.kt
    InventoryUiState.kt
    InventoryEvent.kt
    InventoryEffect.kt
    InventoryViewModel.kt
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

Each `features/<name>/AGENTS.md` must define:

- Feature responsibility.
- Allowed layers.
- Allowed dependencies.
- Models owned by the feature.
- Areas the feature must not touch.
- Minimum validation command.
- Local Definition of Done.

### Module Or Source Set

Create only when real specific rules exist for:

- `shared/src/commonMain`
- `shared/src/androidMain`
- `shared/src/iosMain`
- `androidApp`
- `iosApp`

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
- Current state.
- Target architecture.
- Layer rules.
- Dependency rules.
- Feature example.
- `AGENTS.md` strategy.
- Incremental migration plan.
- Validation.

### 3. Incremental Migration Plan

The document must propose this migration sequence:

1. Freeze architecture rules in documentation.
2. Create `core/designsystem` and `core/ui` only when reusable components exist.
3. Migrate `inventory` as the first pilot feature.
4. Extract `shopping`, `recipes`, `product`, `settings`, and `localai` one by one.
5. Keep hosts as integration layers.
6. Add feature-level `AGENTS.md` files when each feature exists.
7. Run validation after each small migration.

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
./gradlew :shared:compileKotlinAndroid
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
Create the KMP feature-layered MVVM architecture documentation for NeveraChef AI.
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
- ARCH-01 workflow/document created.

Check:
- Not run: documentation-only change.

Risk:
- none
```
