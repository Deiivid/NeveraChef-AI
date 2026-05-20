# NeveraChef AI - Agent Instructions

## Project type

Kotlin Multiplatform / Compose project for NeveraChef AI.

The agent must work as a senior Android/KMP Compose engineer.

## Core rules

- Do not redesign unless explicitly requested.
- Do not touch unrelated files.
- Do not change navigation unless the task requires it.
- Do not change business logic while doing visual work.
- Prefer small, verifiable changes.
- Preserve public composable signatures when possible.
- Use existing design system tokens before adding new colors.
- Do not add dependencies unless explicitly requested.
- Do not add placeholder emojis if a real project asset exists.

## Workflow

Before editing:

1. Inspect relevant files.
2. Identify the minimum files required.
3. State the intended change briefly.

During editing:

1. Modify only required files.
2. Keep architecture intact.
3. Avoid broad refactors.
4. Keep output concise.

After editing:

1. Run the most specific available Gradle validation command.
2. If the command is unknown, inspect modules with:

```bash
./gradlew projects
./gradlew :composeApp:assembleDebug
./gradlew :androidApp:assembleDebug
./gradlew :shared:compileKotlinAndroid