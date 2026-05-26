# Compose Screen Migration Skill

Use this skill when converting Stitch, Pencil, Figma, HTML, Compose mockups or screenshots into Kotlin Multiplatform Compose.

## Use For

- Stitch/Pencil/Figma/HTML to KMP Compose migration
- screenshot to Compose implementation
- existing Compose mockup cleanup into project architecture
- visual fixes against a reference
- one-screen UI implementation from an external source

Do not use for broad architecture refactors, persistence/data-layer work, navigation redesigns or full feature rewrites unless requested.

## Required Inputs

At least one visual source is required:

- screenshot
- HTML/tokens
- Compose mockup
- explicit layout description

Also identify the target Kotlin file before editing.

## Workflow

1. Read `AGENTS.md`.
2. Read `ai/ARCHITECTURE_CONTEXT.md` if structure/state boundaries are involved.
3. Read `ai/PRODUCT_CONTEXT.md` if product behaviour or terminology is involved.
4. Inspect the target screen and nearby screens in the same feature.
5. Inspect existing design system/components only as needed.
6. Translate layout intent into Compose.
7. Edit only required files.
8. Validate using `ai/VALIDATION_ANDROID.md`.

## HTML To Compose

| HTML/CSS | Compose |
|---|---|
| vertical group | `Column` |
| horizontal group / flex row | `Row` |
| layered container | `Box` |
| `gap` | `Arrangement.spacedBy(...)` |
| `padding` | `Modifier.padding(...)` |
| `margin` | parent spacing/padding/arrangement |
| `width: 100%` | `Modifier.fillMaxWidth()` |
| fixed size | `Modifier.width/height/size(...)` only when needed |
| `border-radius` | `RoundedCornerShape(...)` |
| background | `Modifier.background(...)` or component container color |
| border | `Modifier.border(...)` |
| shadow | `shadow(...)` or design-system elevation |
| typography CSS | typography token, `MaterialTheme.typography` or `TextStyle` |
| image/icon | existing resource/icon component when available |
| button/input | existing project component first, then Compose primitive |

Rules:

- Translate layout intent, not generated markup.
- Prefer project tokens/components.
- Preserve callbacks, state, public signatures and navigation.
- Keep screens focused on rendering state and emitting events.
- Do not add business logic to Composables.
- Do not create one-off abstractions unless they clearly improve readability.
- Use KMP shared resources (`Res.*`) inside `commonMain`; do not use platform `R.*`.

## Scope

Allowed edits:

- target screen file
- directly related preview file
- directly related reusable token/component
- directly related assets when required

Forbidden edits:

- unrelated screens/features
- navigation graph unless required
- Gradle files unless required
- persistence/data/domain files for visual-only migrations
- platform-specific Android/iOS files unless required

## Visual Priority

1. layout hierarchy
2. spacing
3. typography
4. color
5. radius/borders
6. icons/images
7. elevation/shadows
8. interaction states

Do not redesign. If exact visual parity conflicts with the design system, prefer the existing design system and note the difference.

## Validation

| Change type | Minimum validation |
|---|---|
| Shared KMP screen | `./gradlew :shared:compileKotlinAndroid` |
| Android-visible screen | `./gradlew :androidApp:assembleDebug` |
| Design-system component | `./gradlew :shared:compileKotlinAndroid` + affected app build |
| Static docs/reference only | Not run; report reason |

Do not claim screenshot verification unless actually performed.

## Prompt Template

```text
Use ai/skills/screen-migration/SKILL.md.

Task:
Convert the provided <Stitch/Pencil/Figma/HTML/screenshot/Compose mockup> into Kotlin Multiplatform Compose.

Target:
<target Kotlin file>

Source:
- Screenshot: <attached/current session/reference path>
- HTML/tokens: <pasted or file path>

Rules:
- Do not redesign.
- Do not touch unrelated files.
- Preserve callbacks, state and navigation.
- Use existing design system tokens/components first.
- Translate HTML/CSS intent into Compose.
- Run the smallest useful Gradle validation after editing.
```

## Output

```text
Files changed:
- ...

Validation:
- ...

Notes:
- ...
```
