# Compose Screen Migration Skill

Use this skill when converting a screen from Stitch, Pencil, Figma, HTML, Compose mockups or screenshots into Kotlin Multiplatform Compose.

The goal is to produce production-ready KMP Compose screens that respect the existing architecture, design system and validation workflow.

---

## When to use this skill

Use this skill for:

- Stitch/Pencil HTML to KMP Compose migration.
- Figma or screenshot to KMP Compose implementation.
- Existing Compose mockup cleanup into project architecture.
- Visual fixes against a reference screenshot.
- One-screen feature implementation where the visual source is external.

Do not use this skill for:

- broad architecture refactors.
- persistence/data-layer work.
- navigation redesigns.
- unrelated UI cleanup.
- full feature rewrites unless explicitly requested.

---

## Workflow

| Phase | Goal | Output |
|---|---|---|
| Intake | Understand source and target. | Source type, target file, constraints. |
| Inspect | Read existing UI patterns. | Components, tokens, state/callbacks. |
| Map | Translate design/HTML into Compose structure. | Layout/component mapping. |
| Implement | Edit only required files. | KMP Compose changes. |
| Validate | Run smallest useful check. | Validation result. |
| Report | Summarize work. | Files changed, validation, notes. |

---

## Inputs

| Input | Required | Notes |
|---|---|---|
| Reference screenshot | Optional | Attached in chat or stored under `references/`. |
| HTML | Optional | Stitch/Pencil exported HTML or pasted markup. |
| Design tokens | Optional | Colors, spacing, radius, typography, shadows. |
| Target Kotlin file | Required | Existing file to modify or requested new screen file. |
| Existing design system | Required to inspect | Use project components/tokens before creating new ones. |
| Behaviour constraints | Required when interactive | State, callbacks, navigation and business rules. |

At least one visual source is required: screenshot, HTML, Compose mockup or explicit layout description.

---

## Source handling

### Fast workflow

Use when the user provides a screenshot/HTML in the current session.

Codex must work from the current session context and must not require repository reference files.

### Persistent workflow

Use when the screen will be revisited across sessions.

Recommended files:

```text
references/<screen-name>.png
.ai/VISUAL_REFERENCE.md
.ai/SCREEN_MIGRATION_NOTES.md
```

---

## Project inspection

Before editing:

1. Read `AGENTS.md`.
2. Read `.ai/ARCHITECTURE_CONTEXT.md` if architecture or feature structure is involved.
3. Read `.ai/VALIDATION_COMMANDS.md` or `VALIDATION_ANDROID.md` before validation.
4. Inspect the target screen file.
5. Inspect nearby screens in the same feature.
6. Inspect `shared/src/commonMain/.../core/designsystem/`.
7. Inspect `shared/src/commonMain/.../core/ui/`.
8. Identify only the required files.

Do not inspect unrelated features unless the target screen clearly reuses their components or patterns.

---

## HTML to Compose mapping

| HTML/CSS concept | Compose equivalent |
|---|---|
| `div` vertical group | `Column` |
| `div` horizontal group / flex row | `Row` |
| absolute/layered container | `Box` |
| `display: flex` | `Row`, `Column`, `Box` depending on direction/layering |
| `gap` | `Arrangement.spacedBy(...)` |
| `padding` | `Modifier.padding(...)` |
| `margin` | Parent spacing, padding or arrangement. Do not blindly translate as padding. |
| `width: 100%` | `Modifier.fillMaxWidth()` |
| fixed width/height | `Modifier.width(...)`, `height(...)`, `size(...)` only when needed |
| `border-radius` | `RoundedCornerShape(...)` |
| `background-color` | `Modifier.background(...)` or component container color |
| `border` | `Modifier.border(...)` |
| `box-shadow` | `shadow(...)` or design-system elevation when available |
| `font-size/font-weight/line-height` | `TextStyle`, typography token or `MaterialTheme.typography` |
| `img/svg/icon` | project drawable/vector asset or existing icon component |
| `button` | existing project button, `Button`, `TextButton` or custom composable if justified |
| `input` | existing text field component or Compose text field |
| `position: absolute` | Avoid by default; use Compose layout primitives unless overlay is required |

Rules:

- Do not copy HTML/CSS literally.
- Translate layout intent, not markup noise.
- Prefer project design tokens over raw literals.
- Keep raw colors/sizes only when no matching token exists.
- Ignore generated class names unless they contain useful token information.

---

## Compose implementation rules

Prefer simple primitives and existing project components:

- `Column`
- `Row`
- `Box`
- `Text`
- `Image`
- `Icon`
- `Button`
- `TextButton`
- `Switch`

Rules:

- Preserve existing state, callbacks, public composable signatures and navigation.
- Keep screen Composables focused on rendering state and emitting events.
- Do not add business logic to Composables.
- Do not create abstractions for a single screen unless clearly reusable.
- Reuse design system colors, typography, spacing, shapes and components when available.
- Avoid expensive work during composition.
- Avoid hardcoded magic values when a token exists.
- Keep accessibility in mind: readable text, usable touch targets and meaningful content descriptions.

---

## State and callbacks

When the target screen already has state/callbacks:

- Preserve public parameters.
- Preserve navigation callbacks.
- Preserve state ownership.
- Do not move state to another layer unless explicitly requested.
- Do not change business behavior while doing visual migration.

When creating a new screen:

- Prefer `State` + `Event` naming when the feature already uses MVVM/MVI.
- Keep the screen mostly stateless when practical.
- Use a `Route` only when wiring state holder/navigation is needed.
- Do not create a ViewModel for static or trivial screens.

---

## File editing rules

Allowed edits:

- target screen file
- directly related preview file
- directly related reusable design token/component
- directly related assets when required

Forbidden edits:

- unrelated screens/features
- navigation graph unless required
- Gradle files unless required
- persistence/data/domain files for visual-only migrations
- platform-specific Android/iOS files unless required

---

## Visual accuracy rules

Prioritize:

1. layout hierarchy
2. spacing
3. typography
4. color
5. radius/borders
6. icons/images
7. elevation/shadows
8. interaction states

Do not redesign. Match the reference while respecting existing app conventions.

If exact visual parity conflicts with the design system, prefer the existing design system and mention the difference in `Notes`.

---

## Validation strategy

| Change type | Minimum validation |
|---|---|
| Shared KMP screen | `./gradlew :shared:compileKotlinAndroid` |
| Android-visible screen | `./gradlew :androidApp:assembleDebug` |
| Design-system component | `./gradlew :shared:compileKotlinAndroid` + affected app build |
| Static documentation/reference only | No build required; state that validation was not run. |

Do not claim screenshot verification was done unless it was actually done.

---

## Prompt template

```text
Use .ai/skills/screen-migration/SKILL.md.

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
- Translate HTML/CSS intent into Compose, do not copy markup literally.
- Run the smallest useful Gradle validation after editing.
```

---

## Final output

Return only:

```text
Files changed:
- ...

Validation:
- ...

Notes:
- ...
```
