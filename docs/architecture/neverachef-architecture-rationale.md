# NeveraChef-AI Architecture Rationale (v1)

Fecha de corte: 2026-05-15  
Alcance: Inicialización del proyecto (sin implementación funcional completa)

## 1. Resumen ejecutivo

NeveraChef-AI arranca con una arquitectura Android-first, local-first y modular para acelerar entrega del MVP sin bloquear evolución futura a KMP.

## 2. Decisiones clave

## ADR-A01 Android-first (aceptada)

Decisión:
- Priorizar Android como plataforma de entrega del MVP.

Motivo:
- Reduce complejidad inicial.
- Permite demo funcional antes.
- Alinea con backlog de Notion (Sprints 2-4).

Trade-off:
- Se retrasa iOS real.

Mitigación:
- Mantener dominio puro y contratos desacoplados para futura extracción a `shared`.

## ADR-A02 Local-first sin login en MVP (aceptada)

Decisión:
- No usar autenticación ni backend propio en la primera entrega.

Motivo:
- Menos riesgo técnico.
- Menor fricción UX.
- Mejora privacidad y velocidad de adopción.

Trade-off:
- Sin sincronización entre dispositivos en MVP.

Mitigación:
- Diseñar capa de datos lista para añadir sync post-MVP.

## ADR-A03 IA con salida JSON estructurada (aceptada)

Decisión:
- Exigir JSON válido en prompts y parsear con DTO + mapper.

Motivo:
- UI más estable.
- Errores detectables.
- Mejor testabilidad.

Trade-off:
- Necesidad de validación y fallbacks.

Mitigación:
- Parser robusto + estados `ErrorState`.

## ADR-A04 Modularización incremental (aceptada)

Decisión:
- Separar `core`, `domain`, `data`, `feature/*` desde el inicio.

Motivo:
- Evitar monolito UI.
- Escalar por flujos/pantallas sin acoplar features.

Trade-off:
- Sobrecoste inicial de setup Gradle.

Mitigación:
- Crear solo módulos estrictamente necesarios en v1.

## 3. Estructura de módulos objetivo (v1)

```text
androidApp/
core/
  common/
  ui/
  designsystem/
  navigation/
domain/
  model/
  recipes/
  shopping/
  preferences/
data/
  repository/
  ai/
  local/
feature/
  onboarding/
  home/
  inventory/
  recipes/
  shopping/
  settings/
shared/   (futuro KMP)
iosApp/   (fuera MVP inicial)
```

## 4. Dependencias por capa

Presentation:
- Compose Material 3
- Navigation Compose
- Lifecycle ViewModel
- Coroutines Android

Domain:
- Kotlin puro
- Coroutines core (si aplica en casos de uso)

Data:
- Kotlinx Serialization
- Cliente HTTP (a decidir entre Ktor/Retrofit)
- DataStore (v1), Room opcional según Sprint 4

Testing:
- JUnit
- MockK
- Turbine

## 5. Flujo técnico de recetas (objetivo Sprint 3)

```text
IngredientInputScreen
 -> ViewModel
 -> GenerateRecipesUseCase
 -> RecipeRepository (domain contract)
 -> AiRecipeRepositoryImpl
 -> RecipePromptBuilder
 -> AiRecipeRemoteDataSource
 -> JSON DTO
 -> Mapper
 -> UiState
 -> RecipeResultsScreen
```

## 6. Alcance por fases

Sprint 2:
- Navegación y pantallas con datos mock.

Sprint 3:
- Integración IA texto real.

Sprint 4:
- Lista compra, preferencias persistidas, menú semanal base.

## 7. Riesgos principales y mitigación

1. JSON inválido desde IA.
- Mitigar con parser defensivo + fallback UI.

2. Scope creep por voz/cámara temprana.
- Mitigar respetando roadmap de Notion (Sprint 5).

3. Acoplar negocio en composables.
- Mitigar con ViewModel + UseCase + modelos de dominio.

## 8. Criterio de éxito técnico de inicialización

Se considera correcta la inicialización si:
1. La app arranca con navegación base y placeholders.
2. Módulos y capas están separadas.
3. El theme y componentes base siguen handoff de diseño.
4. La integración IA queda preparada por contratos, no hardcoded en UI.
