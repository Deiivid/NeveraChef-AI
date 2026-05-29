# Pantry Feature - AGENTS

## Scope
- Feature: inventario (`pantry`).
- Root path: `shared/src/commonMain/kotlin/es/neverachefai/feature/pantry/`.

## Goal
- Mantener la lógica de inventario estable con arquitectura KMP + Compose existente.
- Priorizar cambios pequeños y verificables.

## Architecture Contract
- UI solo renderiza estado y emite eventos.
- No mover lógica de negocio a Composables.
- Mantener separación `domain` / `data` / `ui`.
- No romper contratos de `PantryRepository` y `usecase` salvo solicitud explícita.

## Persistence Contract
- Reutilizar `PantryLocalDataSource` y mapeadores existentes.
- Si se cambia persistencia, debe seguir sobreviviendo reinicios.
- No introducir nuevas dependencias de almacenamiento sin petición explícita.

## Integration Rules
- Mantener compatibilidad con navegación existente.
- Mantener compatibilidad con flujo de compras -> nevera cuando aplique.
- En KMP, conservar código compartido en `commonMain` y plataformas en source set específico.

## Validation
- Preferir validación específica:
  - `./gradlew :shared:compileAndroidMain`
  - `./gradlew :shared:compileKotlinAndroid`
