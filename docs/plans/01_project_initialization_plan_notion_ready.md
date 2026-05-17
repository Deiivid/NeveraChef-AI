# NeveraChef-AI - Plan de Inicialización (Notion Ready)

Fecha de corte: 2026-05-15  
Estado: Propuesta validada contra Notion + Pencil + repo actual

## 1. Alcance del plan

No modificar lógica de negocio real todavía.  
Sí preparar base técnica y documental para ejecución por sprints.

## 2. Trazabilidad de fuentes

Confirmado por Notion:
- MVP Android-first, local-first sin login, IA texto en Sprint 3.

Confirmado por Pencil:
- Flujo y pantallas 01-12 con tokens visuales y componentes.

Confirmado por repo:
- Scaffold KMP base sin modularización de producto.

Inferido:
- Inicialización modular incremental y pragmática.

Pendiente de confirmar:
- Proveedor IA inicial (OpenAI/Gemini).
- Persistencia final (DataStore-only v1 vs Room en Sprint 4).

## 3. Módulos a crear

Prioridad P0:
- `core:common`
- `core:ui`
- `core:designsystem`
- `core:navigation`
- `domain:model`
- `domain:recipes`
- `domain:shopping`
- `domain:preferences`
- `data:repository`
- `data:ai`
- `data:local`
- `feature:onboarding`
- `feature:home`
- `feature:inventory`
- `feature:recipes`
- `feature:shopping`
- `feature:settings`

## 4. Archivos Gradle a tocar

1. `settings.gradle.kts`
- Añadir `include(...)` de módulos nuevos.

2. `build.gradle.kts` (root)
- Mantener plugins compartidos mínimos.

3. `gradle/libs.versions.toml`
- Incorporar versiones y aliases de dependencias nuevas.

4. `androidApp/build.gradle.kts`
- Dependencias a módulos `core/feature/domain/data`.

5. `*/build.gradle.kts` por módulo nuevo
- Plugins + namespace + dependencias por capa.

## 5. Paquetes principales

`core/common`
- result, error, dispatcher provider, utilidades.

`core/designsystem`
- theme, color, typography, spacing, componentes base.

`core/navigation`
- routes, nav graph, args.

`domain/model`
- Ingredient, Recipe, ShoppingItem, UserPreferences, WeeklyMenu, enums.

`domain/recipes`
- RecipeRepository, GenerateRecipesUseCase.

`data/ai`
- prompt builder, dto, parser, mapper, remote datasource.

`feature/*`
- `ui`, `state`, `vm`.

## 6. Dependencias necesarias (mínimas)

P0:
- Compose Material 3
- Navigation Compose
- Lifecycle ViewModel/runtime
- Coroutines
- Kotlin Serialization JSON

P1:
- DataStore Preferences
- Testing: JUnit, MockK, Turbine

Postergadas:
- CameraX, Speech, visión IA
- Crash reporting / analytics
- DI framework avanzado (arrancar con DI manual)

## 7. Estructura de navegación

Onboarding:
- Welcome
- Onboarding
- InitialPreferences

Main (Bottom Navigation):
- Inicio
- Nevera
- Compra
- Ajustes

Rutas internas:
- AddIngredient
- IngredientReview
- RecipeResults
- RecipeDetail

## 8. Theme / Design System

Tokens de handoff:
- Primary `#0066FF`
- Ink `#1A1A1A`
- Muted `#666666`
- Line `#E6E8EC`
- Soft `#F6F8FB`
- AccentSoft `#EAF2FF`

Componentes base v1:
- `PhoneScaffold`
- `AppHeader`
- `PrimaryButton`
- `FoodItemRow`
- `RecipeCard`
- `BottomPillNavigation`
- `LoadingState`
- `EmptyState`
- `ErrorState`

## 9. Pantallas placeholder iniciales

P0:
1. Bienvenida
2. Onboarding
3. Preferencias iniciales
4. Home
5. Nevera/Despensa
6. Añadir alimentos
7. Revisar alimentos
8. Resultados recetas
9. Detalle receta
10. Lista compra
11. Ajustes

Cada una con `UiState` y callbacks, sin llamadas reales de red.

## 10. Integración inicial prevista para IA

Preparar interfaces y contratos, sin activación productiva:
- `RecipeRepository` (domain)
- `GenerateRecipesUseCase`
- `RecipePromptBuilder`
- `AiRecipeRemoteDataSource`
- `RecipeResponseDto`
- `RecipeMapper`
- `AiRecipeRepositoryImpl`
- proveedor mock de IA para pruebas UI

## 11. README inicial profesional

Debe incluir:
1. Visión y alcance MVP
2. Estado real del repo
3. Arquitectura objetivo
4. Roadmap por sprints
5. Flujo principal de usuario
6. Comandos de build/test
7. Enlaces a docs de arquitectura y planes

## 12. Priorización recomendada

P0 (bloqueante):
- Modularización mínima + navegación + placeholders + design system base.

P1 (alta):
- Estados UI reutilizables + persistencia local simple + hardening UX.

P2 (media):
- Integración IA texto completa + menú semanal base.

P3 (posterior):
- Voz/cámara/visión + publicación.
