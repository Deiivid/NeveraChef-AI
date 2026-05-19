# AGENTS.md

## Project

NeveraChefAI is a Kotlin Multiplatform project using Compose Multiplatform.

The app has:
- shared UI/domain code in `shared/`
- Android integration in `androidApp/`
- iOS integration in `iosApp/`
- documentation and prompts in `docs/` and `prompts/`

## Core behavior

Work as a precise Kotlin Multiplatform / Compose Multiplatform coding agent.

Always prefer:
- small changes
- scoped edits
- existing architecture
- existing naming
- existing packages
- existing design system
- Compose Multiplatform-compatible APIs

Do not:
- rewrite unrelated files
- redesign architecture
- touch navigation unless explicitly requested
- create new dependencies unless explicitly requested
- use Android-only APIs inside `shared/src/commonMain`
- invent screens, routes, models, resources or flows
- modify generated/build folders
- modify `.gradle/`, `build/`, `.idea/` unless explicitly requested

## Visual implementation rules

When implementing UI from Pencil, screenshots or visual references:

- Use only the visual reference related to the requested screen.
- Do not reuse images from unrelated screens.
- Do not create placeholder UI if a visual reference exists.
- Match the reference as closely as possible using Compose.
- Use existing assets/resources when available.
- If an asset is missing, report it or add the minimal required resource only if instructed.

## Scope discipline

Before editing, identify the minimum set of files required.

Edit only files directly related to the requested task.

If the task is about one screen, modify only:
- that screen file
- directly related state/model file if needed
- directly related resources if needed

Do not touch other screens.

## Validation

After editing, run the smallest useful validation if possible.

Prefer:
- Kotlin compile check
- Gradle build for the affected module
- static inspection by reading imports/usages

If validation cannot be run, state only:
`Validation: not run`

## Output

Keep output minimal.

After finishing, return only:

```text
Files:
- path/to/file.kt

Validation:
- OK / not run / failed