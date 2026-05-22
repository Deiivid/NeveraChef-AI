Use SCREEN_MIGRATION_SKILL.md and SCREEN_MIGRATION_RULES.md.

Task:
Implement this screen from the attached screenshot and HTML.

Target:
<NombrePantalla>.kt

Priority:
1. Screenshot is the visual source of truth.
2. HTML/tokens define exact colors, spacing, radius and typography.
3. Existing code defines architecture, callbacks, state and navigation.

Rules:
- Do not redesign.
- Do not touch unrelated files.
- If another file is required, justify it before editing.
- Run Gradle validation.
- If emulator is available, install and capture screenshot.

Return only:
Files changed:
- ...

Validation:
- ...

Notes:
- ...
# General Screen Migration Prompt

Use this prompt when you want Codex to convert a Stitch/Pencil/Figma screen into Kotlin Multiplatform Compose.

## How to use it

1. Create or open the target screen in Stitch/Pencil/Figma.
2. Take a screenshot of the final design.
3. Copy the generated HTML/tokens.
4. Open Codex from the repository root.
5. Attach the screenshot to Codex.
6. Paste this prompt.
7. Replace `<ScreenName>.kt` with the real target file name.
8. Paste the HTML/tokens under the `HTML / Tokens` section.

---

## Prompt for Codex

Use `AGENTS.md`, `.ai/SCREEN_MIGRATION_SKILL.md` and `.ai/SCREEN_MIGRATION_RULES.md`.

Task:
Implement the attached Stitch/Pencil/Figma screen in Kotlin Multiplatform Compose.

Target file:
`<ScreenName>.kt`

Reference inputs:
- Attached screenshot: visual source of truth.
- HTML / tokens below: source of truth for colors, spacing, radius and typography.
- Existing project code: source of truth for architecture, callbacks, state and navigation.

Priority order:
1. Screenshot/capture defines the final visual result.
2. HTML/tokens define exact colors, spacing, radius, typography and hierarchy.
3. Existing code defines architecture, callbacks, state, navigation and project conventions.

Execution rules:
- Inspect the target file before editing.
- Detect existing design system colors, typography and icons.
- Modify only the required files.
- Do not redesign.
- Do not invent hierarchy.
- Do not touch navigation.
- Do not change architecture.
- Do not modify unrelated files.
- If another file is required, justify it before editing.
- Preserve existing callbacks, state and public composable signatures where possible.
- Use exact visual tokens from the HTML whenever possible.
- Run Gradle validation after editing.
- If an emulator is available, install the app and capture a screenshot.
- Compare the generated screenshot against the reference and adjust only visual differences.

HTML / Tokens:

```html
<Paste the Stitch/Pencil/Figma HTML here>
```

Final output:

```text
Files changed:
- ...

Validation:
- ...

Notes:
- ...
```