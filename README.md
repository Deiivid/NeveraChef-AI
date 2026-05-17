# NeveraChef-AI

Aplicación móvil enfocada en ayudar a familias y usuarios a cocinar con los ingredientes que ya tienen en casa, apoyándose en IA para generar recetas, ingredientes faltantes y lista de compra.

## Estado actual

Este repositorio parte de un scaffold Kotlin Multiplatform (`androidApp`, `shared`, `iosApp`), pero el proyecto se ejecuta con estrategia:

- Android-first para el MVP.
- Arquitectura limpia modular.
- Enfoque local-first sin login.
- Integración IA por texto en una fase posterior.

Implementación actual:

- Flujo placeholder funcional en UI compartida (`shared`) para Android e iOS.
- Navegación base MVP activa sin lógica de negocio real.

## Objetivo del MVP

1. Introducción manual de ingredientes.
2. Configuración de preferencias básicas.
3. Generación de recetas con IA (texto).
4. Visualización de recetas, detalle y faltantes.
5. Lista de compra automática.
6. Demo técnica clara y defendible.

## Roadmap de ejecución

| Sprint | Enfoque | Resultado esperado |
|---|---|---|
| 0 | Organización | Visión, backlog, ADRs y documentación base |
| 1 | Diseño + arquitectura | Flujo visual y estructura Android-first definida |
| 2 | App mock | Navegación y pantallas placeholder funcionales |
| 3 | IA texto | PromptBuilder + DTO/mapper + repository + use case |
| 4 | Producto | Lista de compra + preferencias + menú semanal base |
| 5 | Multimodal | Voz y cámara/visión |
| 6 | Demo | Guion, README final y Q&A |

## Arquitectura objetivo (v1)

```text
androidApp (host)
  -> feature/*
  -> core/*
  -> domain/*
  -> data/*

shared (reservado para extracción futura KMP)
iosApp (fuera de alcance MVP inicial)
```

Más detalle en:
- `docs/architecture/neverachef-architecture-rationale.md`
- `docs/plans/01_project_initialization_plan_notion_ready.md`

## Diseño visual

El flujo de referencia se basa en:

- `neverachef-codex-handoff.md`
- `Screens 01-12.pdf`
- Tablero Pencil con pantallas 01-12

Componentes UI clave de referencia:
- `PhoneScaffold`
- `AppHeader`
- `PrimaryButton`
- `FoodItemRow`
- `RecipeCard`
- `LoadingState / EmptyState / ErrorState`

## Comandos útiles

```bash
# Build Android
./gradlew :androidApp:assembleDebug

# Tests shared
./gradlew :shared:testAndroidHostTest
./gradlew :shared:iosSimulatorArm64Test
```

## Documentación operativa

Paquete inicial listo para gestión en herramientas externas:

- `docs/plans/01_project_initialization_plan_chatgpt_context.md`
- `docs/plans/01_project_initialization_plan_notion_ready.md`
- `docs/plans/01_project_initialization_plan_notebooklm_pack.md`

Documentación viva del proyecto:

- `docs/PROJECT_CONTEXT.md`
- `docs/PRODUCT.md`
- `docs/ARCHITECTURE.md`
- `docs/UI_SCREENS.md`
- `docs/DESIGN_SYSTEM.md`
- `docs/AI_CONTEXT.md`
- `docs/BACKLOG.md`
- `docs/DECISIONS.md`
- `docs/NOTEBOOKLM_CONTEXT.md`
- `docs/NOTION_SYNC.md`

Prompts de contexto para futuras sesiones:

- `prompts/CODEX_CONTEXT.md`
- `prompts/CHATGPT_CONTEXT.md`
- `prompts/NOTEBOOKLM_PROMPTS.md`

## Fuentes de verdad

1. Notion (visión, backlog, ADR y roadmap).
2. Pencil (pantallas y lenguaje visual).
3. Este repositorio (estado técnico real y decisiones implementadas).
