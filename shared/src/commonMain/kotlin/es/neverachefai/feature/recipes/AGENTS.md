# Recipes Feature - AGENTS

## Scope
- Feature: recetas (`recipes`).
- Root path: `shared/src/commonMain/kotlin/es/neverachefai/feature/recipes/`.

## Goal
- Mantener flujo de generación/resultado/detalle de recetas sin romper arquitectura ni contratos.

## Rules
- UI sin lógica de negocio.
- Reutilizar contratos de dominio existentes.
- No introducir llamadas directas a proveedores AI desde UI.
- Mantener navegación y modelos actuales salvo petición explícita.

## Validation
- `./gradlew :shared:compileAndroidMain`
