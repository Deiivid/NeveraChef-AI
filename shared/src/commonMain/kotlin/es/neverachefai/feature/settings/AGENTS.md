# Settings Feature - AGENTS

## Scope
- Feature: ajustes (`settings`).
- Root path: `shared/src/commonMain/kotlin/es/neverachefai/feature/settings/`.

## Goal
- Mantener configuraciones de usuario con impacto real en UI/lógica donde corresponda.

## Rules
- No hardcodear configuración que ya exista como estado persistido.
- Cualquier umbral o preferencia debe conectar con fuente real de estado/persistencia.
- Mantener patrón de navegación y estado actual.

## Validation
- `./gradlew :shared:compileAndroidMain`
