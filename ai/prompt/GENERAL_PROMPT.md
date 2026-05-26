# General Screen Migration Prompt

Use this prompt to convert a Stitch, Pencil, Figma, HTML or screenshot design into Kotlin Multiplatform Compose.

## Prompt

```text
Use AGENTS.md, ai/skills/screen-migration/SKILL.md and ai/rules/SCREEN_MIGRATION_RULES.md.
Use ai/PRODUCT_CONTEXT.md if the screen touches inventory, shopping, products, amount, weight, location or finalization.
Use ai/VALIDATION_ANDROID.md to choose validation.

Task:
Implement the attached screen in Kotlin Multiplatform Compose.

Target file:
<ScreenName>.kt

Target source set:
shared/commonMain unless the screen is explicitly platform-specific.

Reference inputs:
- Screenshot: visual source of truth.
- HTML/tokens: source of truth for colors, spacing, radius and typography.
- Existing project code: source of truth for architecture, callbacks, state and navigation.

Priority:
1. Existing project code defines architecture, callbacks, state, navigation and product behaviour.
2. Screenshot/capture defines the final visual result.
3. HTML/tokens define exact visual values and hierarchy.

Rules:
- Inspect the target file before editing.
- Inspect nearby screens/components only when needed.
- Use existing design system tokens/components first.
- Modify only required files.
- Do not redesign or invent hierarchy.
- Do not invent product behaviour, navigation flows, empty states or persistence rules.
- Do not touch domain, persistence or navigation unless required by the target screen.
- Do not use Android/iOS framework APIs in shared/commonMain.
- Use shared resources (`Res.*`) in common code; do not use platform `R.*`.
- Preserve callbacks, state, public signatures and navigation.
- Preserve product semantics from ai/PRODUCT_CONTEXT.md when relevant.
- Do not trigger one-off actions from recomposition.
- Use lifecycle-aware effect collection when effects are needed.
- Translate HTML/CSS intent into Compose.
- Run the smallest useful Gradle validation from ai/VALIDATION_ANDROID.md after editing, or report why it was not run.
- If emulator/screenshot tools are available, capture and compare the result.

Acceptance:
- Layout hierarchy matches the screenshot.
- Spacing, typography, colors and radius follow HTML/tokens unless project tokens override them.
- Existing architecture, callbacks, state ownership and product behaviour are preserved.
- No unrelated files are changed.

HTML / Tokens:
<paste HTML/tokens here>

Final output:
Files changed:
- ...

Validation:
- ...

Notes:
- ...
```
