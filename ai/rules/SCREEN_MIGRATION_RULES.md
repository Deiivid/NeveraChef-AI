# Screen Migration Rules

Use these rules when migrating visual designs from HTML, Stitch, Pencil, Figma or screenshots to Kotlin Multiplatform Compose.

## Hard Rules

- Do not redesign.
- Do not invent visual hierarchy.
- Do not change unrelated screens.
- Do not change architecture, navigation, callbacks or behaviour unless required by the task.
- Do not add dependencies unless explicitly requested.
- Use KMP shared resources (`Res.*`) inside `commonMain`; do not use platform `R.*`.

## Visual Fidelity

Prioritize:

1. layout hierarchy
2. spacing
3. typography
4. colors
5. radius/borders
6. selected/unselected states
7. bottom/status/top bar treatment
8. elevation/shadow approximation

Map reference values to project design tokens when possible. Use exact values only when no suitable token exists or visual parity requires it.

## Compose

Prefer simple Compose primitives and existing project components:

- `Column`
- `Row`
- `Box`
- `Text`
- `Icon`
- `Image`
- `Switch`
- `Button` / `TextButton`

Avoid unnecessary abstraction.

## Output

```text
Files changed:
- ...

Validation:
- ...

Notes:
- ...
```
