# NeveraChef-AI - Context Pack for ChatGPT

Fecha: 2026-05-15  
Uso: Pegar como contexto inicial para continuar decisiones/implementación

## 1) Qué es NeveraChef-AI

Aplicación Android-first para cocinar con ingredientes disponibles en casa, con IA para:
- proponer recetas,
- detectar faltantes,
- construir lista de compra,
- (posterior) menú semanal, voz y cámara.

## 2) Estado real del repositorio

- Existe scaffold KMP inicial: `androidApp`, `shared`, `iosApp`.
- No existe todavía arquitectura modular por features.
- README original de plantilla reemplazado por orientación de producto/arquitectura.

## 3) Fuentes de verdad y nivel de certeza

Confirmado por Notion:
- MVP local-first sin login.
- Android-first.
- IA texto en Sprint 3.
- Backlog por sprints 0-6.

Confirmado por Pencil:
- 12 pantallas de referencia con flujo de onboarding -> home -> inventario -> recetas -> compra -> ajustes.
- Tokens visuales y componentes base definidos en handoff.

Confirmado por repo:
- Base técnica actual sin módulos de negocio/lista compra/IA implementados.

Inferido:
- Conviene modularizar ya, sin sobreingeniería de plugins internos ni DI pesada al arranque.

Pendiente:
- Motor IA inicial definitivo (OpenAI/Gemini).
- Momento exacto de Room (vs DataStore solo v1).

## 4) Decisiones de arquitectura (resumen)

1. Android-first para MVP.
2. Local-first sin backend propio en MVP.
3. Respuesta IA estrictamente en JSON.
4. Arquitectura limpia modular incremental.
5. Voz/cámara postergadas a Sprint 5.

## 5) Módulos propuestos a crear

```text
core:common
core:ui
core:designsystem
core:navigation
domain:model
domain:recipes
domain:shopping
domain:preferences
data:repository
data:ai
data:local
feature:onboarding
feature:home
feature:inventory
feature:recipes
feature:shopping
feature:settings
```

## 6) Navegación objetivo v1

Onboarding graph:
- Welcome
- Onboarding
- InitialPreferences

Main graph (bottom nav):
- Home
- Inventory
- Recipes
- Shopping
- Settings

Rutas secundarias:
- IngredientInput
- IngredientReview
- RecipeDetail/{id}

## 7) Pantallas placeholder iniciales

1. WelcomeScreen
2. OnboardingScreen
3. PreferencesSetupScreen
4. HomeScreen
5. InventoryScreen
6. IngredientInputScreen
7. IngredientReviewScreen
8. RecipeResultsScreen
9. RecipeDetailScreen
10. ShoppingListScreen
11. SettingsScreen

## 8) Integración IA prevista (sin implementar ahora)

- `RecipeRepository` (domain contract)
- `GenerateRecipesUseCase`
- `RecipePromptBuilder`
- `AiRecipeRemoteDataSource`
- `RecipeResponseDto`
- `RecipeMapper`
- `AiRecipeRepositoryImpl`
- `FakeAiRecipeRemoteDataSource` para desarrollo

## 9) Prompt sugerido para continuar con ChatGPT

```text
Actúa como arquitecto Android senior. Con este contexto de NeveraChef-AI, genera el plan de implementación Sprint 2 en tareas atómicas (2-4h por tarea), con orden de ejecución, dependencias y Definition of Done por tarea. Respeta Android-first, local-first, arquitectura limpia modular y evita sobreingeniería.
```
