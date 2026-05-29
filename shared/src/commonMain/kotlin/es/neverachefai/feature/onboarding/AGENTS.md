# Onboarding Feature - AGENTS

## Scope
- Feature: onboarding.
- Root path: `shared/src/commonMain/kotlin/es/neverachefai/feature/onboarding/`.

## Goal
- Preservar flujo inicial y preferencias sin regressions.

## Rules
- Mantener modelos de dominio y estado inmutables existentes.
- No romper persistencia de preferencias iniciales.
- Mantener UI simple y consistente con el resto de la app.

## Validation
- `./gradlew :shared:compileAndroidMain`
