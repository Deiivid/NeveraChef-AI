# ARCH-01 - KMP Feature-Layered Architecture

## Goal

Define NeveraChef AI as an Android-first Kotlin Multiplatform project using a feature-layered architecture.

All shared product code lives under:

```text
shared/src/commonMain/kotlin/es/neverachefai/
```

Each feature is a self-contained vertical slice with its own `data`, `domain`, and `ui` layers. There is no global application `domain` layer.

## Project Structure

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
        dto/
        mapper/
        HomeLocalDataSource.kt
        HomeRemoteDataSource.kt
        HomeRepositoryImpl.kt
      domain/
        model/
        usecase/
        HomeRepository.kt
      ui/
        HomeScreen.kt
        HomeViewModel.kt
        HomeState.kt
        HomeEvent.kt
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
      NavGraph.kt
      routes/
```

Android and iOS hosts remain integration layers:

```text
androidApp/
iosApp/
shared/src/androidMain/
shared/src/iosMain/
```

## Strict Layer Rules

### Dependency Direction

```text
ui -> domain <- data
```

- `ui` depends on `domain` only.
- `data` depends on `domain` only.
- `domain` depends on no application layer.
- `ui` and `data` must never import from each other.

### `ui`

Owns UI behavior:

- Compose screens.
- ViewModels.
- UI state.
- UI events.
- UI effects when needed.
- UI-only formatting and mapping.

Rules:

- ViewModels call use cases from `domain/usecase`.
- Screens render state and emit events.
- Screens do not call repositories, datasources, persistence, preferences, network, or AI providers directly.
- `ui` must not import from the feature `data` package.

### `domain`

Owns feature business logic:

- Feature-owned models.
- Use cases.
- Repository interfaces.
- Domain validation.
- Domain errors and result models.

Rules:

- `domain` is per-feature.
- There is no global `shared/domain` package.
- Repository interfaces live in `domain`.
- Repository implementations live in `data`.
- `domain` must not depend on Compose, Android, iOS, persistence, network, provider SDKs, DTOs, or UI state.

### `data`

Owns concrete data access:

- Repository implementations.
- Local datasources.
- Remote datasources when needed.
- Provider datasources when needed.
- DTOs.
- Persistence models.
- Mappers.

Rules:

- `data` implements repository interfaces defined in `domain`.
- Local storage is accessed through `data` datasources.
- Storage details must not leak into `domain` or `ui`.
- `data` must not import from the feature `ui` package.

## Local-First Data Rule

NeveraChef AI is local-first by default.

Feature data is stored behind local datasources:

```text
feature/<name>/data/<Feature>LocalDataSource.kt
```

The dependency chain is:

```text
ui ViewModel -> domain UseCase -> domain Repository interface <- data RepositoryImpl -> data LocalDataSource
```

No composable, screen, route, state, or event class should know how data is stored.

## Feature-Specific Application Rules

- `feature/pantry` (`Nevera`) uses `data + domain + ui` and persists local data through `data` datasources.
- `feature/shopping` (`Lista de compra`) uses `data + domain + ui` and persists local data through `data` datasources.
- `feature/recipes` uses `domain + ui` as the stable flow contract, and routes generation through a repository contract in `domain`.
- `feature/recipes/data` is the provider-facing layer for AI integration (for example `AiRecipesDataSource` and repository implementation). UI must not call AI providers directly.

## No Cross-Feature Imports

Feature internals are private.

Forbidden:

```text
feature/pantry -> feature/recipes
feature/recipes -> feature/shopping
feature/settings -> feature/pantry/data
```

If two features need a shared model or utility, move it to `core` only when reuse is real.

## `core` Rules

`core` is infrastructure and shared UI foundation only.

Allowed:

- `core/designsystem`: shared theme, tokens, shapes, typography, reusable design primitives.
- `core/persistence`: KMP database setup, driver factories, persistence infrastructure.
- `core/preferences`: DataStore or multiplatform settings infrastructure.
- `core/ui`: KMP ViewModel base, common UI extensions, common UI utilities.

Forbidden:

- Feature-specific business logic.
- Feature-specific repository implementations.
- Feature-specific models.
- Direct dependencies on feature packages.

Features can depend on `core`; `core` must not depend on features.

## Dependency Injection

Each feature should expose a DI module when it has dependencies to bind.

Rules:

- The feature module binds repository interfaces to data implementations.
- The feature module provides use cases and ViewModels when needed.
- The `app` layer assembles all feature modules.
- Do not introduce a DI framework unless the project already uses one or the task explicitly asks for it.

Example shape:

```text
feature/pantry/
  PantryModule.kt
  data/
    PantryRepositoryImpl.kt
  domain/
    PantryRepository.kt
    usecase/
  ui/
    PantryViewModel.kt
```

## Naming Conventions

| Element | Convention | Example |
|---|---|---|
| Repository interface | `<Feature>Repository` | `PantryRepository` |
| Repository implementation | `<Feature>RepositoryImpl` | `PantryRepositoryImpl` |
| Use case | verb + noun + `UseCase` | `GetPantryItemsUseCase` |
| ViewModel | `<Feature>ViewModel` | `PantryViewModel` |
| UI state | `<Feature>State` | `PantryState` |
| UI event | `<Feature>Event` | `PantryEvent` |
| DTO | `<Entity>Dto` | `PantryItemDto` |
| Mapper | `<Entity>Mapper` extension functions | `PantryItemMapper` |

## KMP Rules

- `expect`/`actual` declarations go in `core/persistence` or `core/preferences` when they are infrastructure concerns.
- ViewModels use a KMP-compatible base class from `core/ui`, not `androidx.lifecycle` directly in `commonMain`.
- `viewModelScope` must come from the KMP ViewModel base or an equivalent shared abstraction.
- No Android-specific imports such as `android.*` or `androidx.*` are allowed in `commonMain`.
- Platform-specific implementations belong in `androidMain` or `iosMain`.

## Navigation

Navigation belongs under:

```text
feature/navigation/
```

It owns:

- NavGraph.
- Routes.
- NavHost wiring.

Navigation may reference public screen entry points, but it must not depend on feature `data` internals.

## Adding A New Feature

1. Create `feature/<name>/data`, `feature/<name>/domain`, and `feature/<name>/ui`.
2. Define repository interfaces in `domain`.
3. Add models under `domain/model`.
4. Add use cases under `domain/usecase`.
5. Implement repositories in `data`.
6. Put local storage access in `data` datasources.
7. Create ViewModels and screens in `ui`.
8. Bind dependencies in the feature DI module when needed.
9. Register the feature module in `app`.
10. Add routes in `feature/navigation`.

Never create files directly under `feature/<name>` unless they are feature-level wiring such as DI.

## Current Migration Notes

The repository already has these feature folders:

```text
feature/home
feature/navigation
feature/onboarding
feature/pantry
feature/recipes
feature/settings
feature/shopping
```

ARCH-01 should be applied incrementally:

1. Keep the existing `feature/` root.
2. Remove reliance on any global `domain/` package over time.
3. Move feature-owned models into `feature/<name>/domain/model`.
4. Move feature-owned business actions into `feature/<name>/domain/usecase`.
5. Move feature-owned UI into `feature/<name>/ui`.
6. Move storage access behind `feature/<name>/data` datasources.
7. Validate after each small feature migration.

## Definition Of Done

- Each feature has `data`, `domain`, and `ui` when it has real code for those layers.
- `ui -> domain <- data`.
- No global `shared/domain` for feature-owned business logic.
- Repository interfaces are in `domain`.
- Repository implementations are in `data`.
- Local storage is behind `data` datasources.
- ViewModels call use cases.
- Screens render immutable state and emit events.
- No feature imports another feature's internals.
- No Android or iOS APIs leak into `shared/src/commonMain`.

## Validation

For documentation-only changes:

```text
Not run: documentation-only change.
```

For source/package changes in `shared`:

```bash
./gradlew :shared:compileKotlinAndroid
```

For Android host changes:

```bash
./gradlew :androidApp:assembleDebug
```
