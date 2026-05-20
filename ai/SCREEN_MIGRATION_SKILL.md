# Compose Screen Migration Skill

Use this skill when converting a screen from Stitch, Pencil, Figma, HTML or a screenshot into Kotlin Multiplatform Compose.

## Main rule

The reference screenshot does not need to be stored in the repository for quick one-screen tasks.

Codex can work from:
- a screenshot attached in the current chat/session
- HTML pasted in the prompt
- design tokens pasted in the prompt
- a target Kotlin file name

Store screenshots in the repo only when the same reference must be reused across sessions or by multiple agents.

## Valid workflows

### Fast workflow

Use this for quick implementation or visual fixes.

Inputs are passed directly in the Codex prompt:
- screenshot
- HTML/design tokens
- target file
- constraints

Codex must implement the screen from the current session context without requiring repository reference files.

### Persistent workflow

Use this when the screen will be revisited several times.

Recommended files:
- `references/<screen-name>.png`
- `.ai/VISUAL_REFERENCE.md`

Use this when:
- multiple sessions are needed
- several agents/subagents will work on the same screen
- screenshot comparison will be repeated
- the reference must remain documented in the repo

## Inputs

Expected inputs:
- reference screenshot, attached in chat or stored in `references/`
- HTML or design tokens
- target Kotlin file
- existing design system files

## Process

1. Read `AGENTS.md` if it exists.
2. Read `.ai/VISUAL_REFERENCE.md` if it exists and is relevant.
3. Inspect the target screen.
4. Inspect existing design system colors, typography and icons.
5. Identify only the required files.
6. Modify only the required files.
7. Preserve existing state, callbacks, public composable signatures and navigation.
8. Build the project.
9. If an emulator is available, install the app and capture a screenshot.
10. Compare visually and adjust only layout, spacing, colors, radius, borders and typography.

## Compose rules

Prefer simple primitives:
- Column
- Row
- Box
- Text
- Switch
- Button
- TextButton

Avoid unnecessary abstractions.

## Forbidden

- Do not redesign.
- Do not touch unrelated files.
- Do not change navigation.
- Do not introduce dependencies.
- Do not rewrite architecture.
- Do not remove business logic.
- Do not require repository screenshots when the user already provided a screenshot in the current session.
- Do not use emoji placeholders when project assets exist.

## Prompt usage

Example prompt:

```text
Use SCREEN_MIGRATION_SKILL.md.

Task:
Convert the attached Stitch screenshot and HTML into Kotlin Multiplatform Compose.

Target:
SettingsScreen.kt

Rules:
- Do not redesign.
- Do not touch unrelated files.
- Preserve callbacks, state and navigation.
- Use exact visual tokens from the HTML.
- Run Gradle build after editing.
- If emulator is available, install and capture a screenshot.

Final output:
Files changed:
- ...

Validation:
- ...

Notes:
- ...
```

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