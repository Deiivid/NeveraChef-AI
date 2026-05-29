# Shopping Feature - AGENTS

## Scope
- Feature: lista de compra (`shopping`).
- Root path: `shared/src/commonMain/kotlin/es/neverachefai/feature/shopping/`.

## Goal
- Implementar y ajustar UI/flujo de compra respetando referencia visual y arquitectura existente.

## Architecture Contract
- Mantener patrón actual de estado/eventos/callbacks.
- No extraer lógica de negocio a Composables.
- Reutilizar `domain` (`usecase`, `repository`) y `data` (`datasource`, `mapper`) existentes.
- No crear arquitectura nueva ni refactors amplios sin solicitud explícita.

## UI Contract
- Respetar proporciones, espaciados y jerarquía visual de referencia cuando exista diseño objetivo.
- Mantener coherencia con tokens/componentes ya usados en el proyecto.

## Persistence Contract
- Cambios en lista de compra deben persistir reinicios.
- Acciones como marcar/eliminar/finalizar compra deben persistir en almacenamiento local existente.
- Si `finalizar compra` integra con nevera, usar flujo normal de repositorios y persistencia local.

## Validation
- Preferir validación específica:
  - `./gradlew :shared:compileAndroidMain`
  - `./gradlew :shared:compileKotlinAndroid`
