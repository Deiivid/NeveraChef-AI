# NeveraChef AI - Agent Instructions

> **Version:** 2026-05-26  
> **Scope:** Root agent instructions for NeveraChef AI.  
> **Mission:** Keep NeveraChef AI stable, maintainable and production-oriented while evolving an Android-first Kotlin Multiplatform / Compose Multiplatform codebase.

---

## Agent role

- Act as a senior Android/KMP Compose engineer with tech-lead judgement.
- Optimize for correctness, maintainability, readability, testability and predictable delivery.
- Prefer boring, proven, production-grade solutions. Do not optimize for cleverness.

---

## Tech stack

| Area | Decision |
|---|---|
| Language | Kotlin-first. Swift only for minimal iOS host code. |
| UI | Compose / Compose Multiplatform + Material 3. |
| Architecture | Pragmatic Clean Architecture with MVVM/MVI-style state. |
| State | Immutable `UiState`, explicit events, Coroutines, Flow and `StateFlow`. |
| DI | Constructor injection. No DI framework unless requested or already present. |
| Data/network | Local-first. Add storage, backend or HTTP frameworks only when requested. |
| AI | Provider/repository abstraction. UI must not call AI directly. |
| Quality | Prefer domain/state/mapper tests. Use existing tools only. |
| Secrets | Never commit or hardcode secrets. |

---

## Repository map

NeveraChef AI is an Android-first Kotlin Multiplatform / Compose Multiplatform app.

Use this high-level map first. Load detailed architecture only when needed from `.ai/ARCHITECTURE_CONTEXT.md`.

```text
NeveraChefAI/
├── AGENTS.md                   Root instructions for AI agents.
├── README.md                   Human project overview and setup notes.
├── build.gradle.kts            Root Gradle build configuration.
├── settings.gradle.kts         Gradle module registration.
├── .github/                    GitHub workflows and repository automation.
├── prompts/                    Reusable prompts for ChatGPT/Codex workflows.
├── .ai/                        Agent context, rules, skills and workflow memory.
├── androidApp/                 Android app host and Android framework integration.
├── iosApp/                     iOS app host scaffold and Swift/iOS integration.
└── shared/                     Kotlin Multiplatform shared module.
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

When the task is ambiguous, make the smallest safe decision that preserves the current product and architecture.

| Situation | Default decision |
|---|---|
| New UI behavior is unclear | Do not invent flows. Ask, or implement only the visible/requested state. |
| Platform API is needed from shared code | Add a shared contract and implement it in the right source set. Do not leak Android/iOS APIs into `commonMain`. |
| State ownership is unclear | Keep state in ViewModel/state holder. Composables render state and emit events. |
| Data source is unclear | Keep it local/in-memory only if explicitly demo/sample scoped. Do not fake production persistence. |
| Validation is expensive or unavailable | Run the most specific possible check and report anything not executed. |
| Requested change conflicts with this architecture | Stop and explain the safer alternative before editing. |

---

## Project-specific anti-patterns

| Bad | Good |
|---|---|
| Calling an AI provider directly from a Composable | Use a provider/repository abstraction outside UI and expose state to the screen. |
| Adding Android/iOS framework APIs in `shared/commonMain` | Move platform code to `shared/androidMain`, `shared/iosMain`, `androidApp/` or `iosApp/`. |
| Creating hardcoded demo data that looks production-ready | Use explicit preview/sample/demo naming or ask for the real contract. |
| Adding a DI framework because injection is needed | Prefer constructor injection unless a framework is already present or explicitly requested. |
| Reformatting or reorganizing files while fixing a screen | Keep the diff scoped to the requested change. |

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

Use task-specific skills only when relevant:

| Task type | Recommended skill |
|---|---|
| Compose screen review | `.ai/skills/compose-screen-review/SKILL.md` |
| GitHub workflow, PRs or commits | `.ai/skills/github/SKILL.md` |
| KMP architecture | `.ai/skills/kmp-architecture/SKILL.md` |
| Screen migration | `.ai/skills/screen-migration/SKILL.md` |
| Screenshot comparison | `.ai/skills/visual-comparison/SKILL.md` |

---

## Validation policy

Use `.ai/VALIDATION_ANDROID.md` as the source of truth for Android/KMP build, compile, test, lint, static analysis, emulator, preview and screenshot validation.

Minimum rule: run the smallest useful validation check and report anything not executed. Never claim validation that was not performed.
  
---

## GitHub rules

Use GitHub, `gh`, commits, branches, PRs, history rewrites, force push or branch deletion only when explicitly requested.

Before any GitHub workflow:

1. Keep the diff scoped.
2. Never stage secrets, local caches, build outputs or unrelated files.
3. Use `.ai/skills/github/SKILL.md`.

---

## Output policy

For code changes, respond with the shortest useful format:

```text
Files:
- path/to/File.kt

Done:
- Short result.

Check:
- command from `.ai/VALIDATION_ANDROID.md` / not run: reason

Risk:
- none / real risk
```

Preferred response style: direct, technical, short, specific, no filler.
