# Screen migration rules

## Goal

Migrate visual designs from HTML/Stitch/Pencil/Figma to Kotlin Multiplatform Compose.

## Hard rules

- Do not redesign.
- Do not invent visual hierarchy.
- Do not change unrelated screens.
- Do not change app architecture.
- Do not add new dependencies unless explicitly requested.
- Do not replace existing navigation.
- Keep behavior and callbacks intact.

## Visual fidelity

Translate the reference using exact values where possible:

- colors
- spacing
- typography
- radius
- borders
- card elevation/shadow approximation
- selected/unselected states
- bottom navigation overlap
- status/top bar visual treatment

## Compose implementation

Prefer:

- Column
- Row
- Box
- Text
- Switch
- Button/TextButton
- simple custom composables

Avoid unnecessary abstraction.

## Output

Keep final answer short:

Files changed:
- ...

Validation:
- ...

Notes:
- ...