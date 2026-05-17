# NeveraChef-AI - NotebookLM Knowledge Pack v1

Fecha de corte: 2026-05-15  
Propósito: Base documental unificada para consultas, resumen de decisiones y soporte de ejecución.

## A. Contexto del producto

NeveraChef-AI es una app móvil que ayuda a decidir qué cocinar con ingredientes ya disponibles en casa.  
El valor principal está en transformar inventario doméstico y preferencias en recetas accionables y lista de compra.

Problema que resuelve:
- No saber qué cocinar con lo que hay.
- Desperdicio de alimentos.
- Menús repetitivos.
- Compras desorganizadas.

## B. Fuentes y jerarquía de verdad

1. Notion:
- visión de producto,
- backlog por sprints,
- ADR,
- alcance MVP.

2. Pencil:
- flujo visual y pantallas de referencia.

3. Repositorio:
- estado técnico real implementado.

Regla:
- si hay conflicto entre diseño deseado y código, prevalece código para estado actual y Notion para dirección del producto.

## C. Decisiones arquitectónicas (resumen)

1. Android-first para MVP.
- Justificación: rapidez, menor riesgo, entregable defendible.

2. Local-first sin login.
- Justificación: privacidad, menor complejidad, menos fricción.

3. IA en JSON estructurado.
- Justificación: robustez de parseo y estabilidad UI.

4. Modularización limpia incremental.
- Justificación: escalabilidad sin monolito.

5. Voz/cámara post-MVP temprano.
- Justificación: no bloquear valor principal por complejidad multimodal.

## D. Modelo de ejecución por sprints

Sprint 0:
- organización documental y backlog.

Sprint 1:
- arquitectura y diseño.

Sprint 2:
- app mock navegable con estados UI.

Sprint 3:
- integración IA texto.

Sprint 4:
- preferencias y lista de compra.

Sprint 5:
- voz y visión/cámara.

Sprint 6:
- demo final y documentación de defensa.

## E. Estructura técnica objetivo

Módulos:
- `core`: common, ui, designsystem, navigation
- `domain`: model, recipes, shopping, preferences
- `data`: repository, ai, local
- `feature`: onboarding, home, inventory, recipes, shopping, settings

Estado actual del repo:
- scaffold KMP inicial con `androidApp`, `shared`, `iosApp`.
- pendiente migración a estructura modular de producto.

## F. Flujo funcional canónico

Bienvenida  
-> Onboarding  
-> Preferencias iniciales  
-> Home  
-> Añadir ingredientes  
-> Revisar ingredientes  
-> Generar recetas  
-> Resultados  
-> Detalle de receta  
-> Lista de compra  
-> Ajustes

## G. Integración IA prevista (Sprint 3)

Contrato de dominio:
- `RecipeRepository`
- `GenerateRecipesUseCase`

Capa data:
- `RecipePromptBuilder`
- `AiRecipeRemoteDataSource`
- `RecipeResponseDto`
- `RecipeMapper`
- `AiRecipeRepositoryImpl`

Requisitos:
- salida JSON válida,
- validación de errores,
- estados loading/error/empty.

## H. Design system de referencia

Colores base:
- Primary `#0066FF`
- Ink `#1A1A1A`
- Muted `#666666`
- Line `#E6E8EC`
- Soft `#F6F8FB`
- AccentSoft `#EAF2FF`

Tipografía:
- Heading: Inter
- Body: Geist
- Caption: Funnel Sans

Componentes base:
- PhoneScaffold
- AppHeader
- PrimaryButton
- FoodItemRow
- RecipeCard
- BottomPillNavigation
- LoadingState / EmptyState / ErrorState

## I. Riesgos y mitigaciones

Riesgo 1: JSON inválido desde IA  
Mitigación: parser defensivo + fallback UI + trazas de error.

Riesgo 2: scope creep por voz/cámara prematuros  
Mitigación: respetar orden de sprints.

Riesgo 3: acoplar lógica en composables  
Mitigación: ViewModel + UseCase + Domain puro.

Riesgo 4: sobreingeniería de build/DI  
Mitigación: setup mínimo, incremental, sin frameworks extra no críticos.

## J. Preguntas frecuentes (FAQ)

Q: Por qué Android-first si hay KMP?  
A: Porque reduce riesgo de entrega; se deja KMP-ready para extracción futura.

Q: Por qué local-first sin backend?  
A: Permite validar valor del producto rápido y con menor complejidad operativa.

Q: Cuándo entra voz/cámara?  
A: Sprint 5, después de validar flujo principal de recetas por texto.

Q: Qué significa éxito del MVP?  
A: Flujo completo con ingredientes manuales, recetas IA, faltantes y lista de compra en app navegable y documentada.

## K. Prompt de consulta recomendado para NotebookLM

```text
Usa este pack como fuente principal. Resume el estado actual de NeveraChef-AI y propón las próximas 5 tareas más críticas para completar Sprint 2, incluyendo dependencias y criterios de aceptación.
```
