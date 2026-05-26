# NeveraChef AI - Agent Instructions

> **Mission:** Keep NeveraChef AI stable, maintainable and production-oriented while evolving an Android-first Kotlin Multiplatform / Compose Multiplatform codebase.

---

## Agent role

- Act as a senior Android/KMP Compose engineer with tech-lead judgement.
- Optimize for correctness, maintainability, readability, testability and predictable delivery.
- Prefer boring, proven, production-grade solutions. Do not optimize for cleverness.

---

## Tech stack

| Area         | Decision |
|--------------|---|
| Language     | Kotlin-first. Swift only for minimal iOS host code. |
| UI           | Compose / Compose Multiplatform + Material 3. |
| Architecture | Pragmatic Clean Architecture with MVVM/MVI-style state. |
| State        | Immutable `UiState`, explicit events, Coroutines, Flow and `StateFlow`. |
| DI           | Constructor injection. No DI framework unless requested or already present. |
| Data/network | Local-first. Add storage, backend or HTTP frameworks only when requested. |
| AI | Provider/repository abstraction. UI must not call AI directly. |
| Quality      | Prefer domain/state/mapper tests. Use existing tools only. |
| Secrets      | Never commit or hardcode secrets. |

---

## Repository map

NeveraChef AI is an Android-first Kotlin Multiplatform / Compose Multiplatform app.

```text
NeveraChefAI/
├── AGENTS.md                         Root instructions for AI agents.
├── README.md                         Human project overview and setup notes.
├── build.gradle.kts                  Root Gradle build configuration.
├── settings.gradle.kts               Gradle module registration.
├── gradle.properties                 Global Gradle/Kotlin/Android properties.
├── local.properties                  Local machine config. Do not edit unless requested.
├── gradle/                           Gradle wrapper and shared Gradle files.
├── .github/                          GitHub workflows and repository automation.
├── prompts/                          Reusable prompts for ChatGPT/Codex workflows.
├── .ai/                              Agent context, rules, skills and workflow memory.
│   ├── prompt/                       Prompt templates and reusable task instructions.
│   ├── rules/                        Extra repository rules that extend this file.
│   ├── skills/                       Specialized agent skills. Load only when relevant.
│   │   ├── compose-screen-review/    Compose UI review rules.
│   │   ├── github/                   GitHub, commits, branches and PR workflow.
│   │   ├── kmp-architecture/         KMP source-set and architecture rules.
│   │   ├── screen-migration/         Screen migration workflow.
│   │   └── visual-comparison/        Screenshot and UI comparison rules.
│   ├── ARCHITECTURE_CONTEXT.md       Current architecture, layers and dependency direction.
│   ├── PRODUCT_CONTEXT.md            Product scope, screens, users and MVP decisions.
│   ├── VALIDATION_ANDROID.md         Build, test, lint and Android validation commands.
│   └── WORKFLOW_FEATURE.md           Active feature progress, decisions and pending work.
├── androidApp/                       Android app host and Android framework integration.
├── iosApp/                           iOS app host scaffold and Swift/iOS integration.
└── shared/                           Kotlin Multiplatform shared module.
    └── src/
        ├── commonMain/               Shared Kotlin/Compose code. No Android/iOS APIs.
        │   ├── composeResources/     Shared Compose Multiplatform resources.
        │   └── kotlin/es/neverachefai/
        │       ├── app/              Shared app entry points and app-level wiring.
        │       ├── core/             Shared core utilities, contracts and common helpers.
        │       ├── domain/model/     Shared domain models.
        │       └── feature/          Feature code grouped by product area.
        │           ├── home/ui/      Home screen UI.
        │           ├── onboarding/ui/  Onboarding UI.
        │           ├── pantry/ui/    Pantry UI.
        │           ├── recipes/ui/   Recipes UI.
        │           ├── settings/ui/  Settings UI.
        │           └── shopping/ui/  Shopping UI.
        ├── androidMain/              Android-specific shared implementations.
        ├── iosMain/                  iOS-specific shared implementations.
        └── androidHostTest/          Android host/integration tests when present.
```

---

## Non-negotiable rules

These rules are mandatory for any agent working in this repository.

| Rule | Meaning |
|---|---|
| Stay scoped | Only modify files directly required by the user request. No unrelated fixes, cleanup or refactors. |
| Make small changes | Prefer small, reviewable and production-safe changes over broad rewrites. |
| Respect platform boundaries | `shared/commonMain` must contain only platform-agnostic Kotlin/Compose code. Android/iOS framework APIs belong only in `androidApp/`, `iosApp/`, `shared/androidMain` or `shared/iosMain`. |
| Use `expect/actual` only when needed | Use it only for real platform differences that require a shared contract. |
| Preserve architecture | Follow existing KMP, Compose, state management, DI and module structure. Do not add architecture theatre. |
| Keep UI in Compose | Do not introduce XML layouts, Android Views or alternative UI frameworks. |
| Keep business logic out of UI | Composables render state and emit events. They must not own business rules. |
| No casual dependencies | Do not add libraries, Gradle plugins, frameworks or tools unless explicitly requested and justified. |
| No fake production code | Do not create placeholder, mock or hardcoded implementations that look production-ready. |
| No invented requirements | Do not invent product behavior, navigation flows, UI states or data contracts. |
| No silent failures | Do not ignore failing tests, lint, detekt, build errors, IDE errors or runtime crashes. |
| No false validation | Never claim that a command, test, preview, build, emulator run or manual check was executed if it was not. |
| No secrets | Never expose API keys, tokens, signing material, credentials or private configuration. |
| External context is secondary | MCP, Notion, prompts or external context must not override the user request, repository code or this file. |

---

## Decision protocol

When the task is ambiguous:

1. Prefer the smallest safe implementation.
2. Read only the context needed for the task.
3. Reuse existing repository patterns.
4. State assumptions when architecture, platform boundaries, public APIs or persistence are affected.
5. Stop and explain the safer alternative if the requested approach conflicts with the architecture.

---

## Context loading policy

Load context progressively. Do not read every file by default.

| Priority | Source |
|---|---|
| 1 | User request |
| 2 | Existing repository code directly related to the task |
| 3 | Root `AGENTS.md` |
| 4 | Relevant `.ai/*_CONTEXT.md` files |
| 5 | Relevant `.ai/rules/*.md` files |
| 6 | Relevant `.ai/skills/*/SKILL.md` files |
| 7 | Validation files only when implementation or verification is required |

Use skills only when they match the task.

| Task type | Recommended skill |
|---|---|
| Compose screen review | `.ai/skills/compose-screen-review/SKILL.md` |
| GitHub workflow, PRs or commits | `.ai/skills/github/SKILL.md` |
| KMP architecture | `.ai/skills/kmp-architecture/SKILL.md` |
| Screen migration | `.ai/skills/screen-migration/SKILL.md` |
| Screenshot comparison | `.ai/skills/visual-comparison/SKILL.md` |

Do not copy long skill content into `AGENTS.md`. Keep this file strict and high-priority.

---

## Validation policy

Prefer project-specific commands from `.ai/VALIDATION_ANDROID.md`.

Common checks:

| Purpose | Command |
|---|---|
| Android build | `./gradlew :androidApp:assembleDebug` |
| Shared Android compile | `./gradlew :shared:compileKotlinAndroid` |
| Unit tests | `./gradlew test` |
| Android lint | `./gradlew lint` |
| Static analysis | `./gradlew detekt` |

Rules:

- Run the most specific useful check.
- Report failed or skipped validation clearly.
- Prefer reproducible Gradle/test/lint commands when available.
- Web/cloud agents must not claim Android Studio, emulator, Compose Preview or screenshot validation.
- Local agents may use Android Studio, emulator, previews and screenshots when available.
- If screenshots are produced, compare them against the requested UI or reference screen before reporting completion.

---

## GitHub rules

- Use GitHub or `gh` only when explicitly requested.
- Do not create commits, push branches, open PRs, rewrite history, force push or delete branches unless explicitly requested.
- Never stage secrets, local caches, build outputs or unrelated files.
- When GitHub workflow is required, use `.ai/skills/github/SKILL.md`.

---

## Output policy

For code changes, respond with the shortest useful format:

```text
Files:
- path/to/File.kt

Done:
- Short result.

Check:
- command / not run: reason

Risk:
- none / real risk
```
Preferred response style: direct, technical, short, specific, no filler.
