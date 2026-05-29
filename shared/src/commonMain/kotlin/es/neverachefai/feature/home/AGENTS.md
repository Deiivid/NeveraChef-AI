# Home Feature - AGENTS

## Scope
- Feature: home.
- Root path: `shared/src/commonMain/kotlin/es/neverachefai/feature/home/`.

## Goal
- Mantener pantalla principal como capa de orquestación visual sin lógica de dominio acoplada.

## Rules
- Evitar lógica de negocio en composables.
- Reusar navegación/estado ya definidos.
- Cambios visuales: mantener consistencia de diseño global.

## Validation
- `./gradlew :shared:compileAndroidMain`
