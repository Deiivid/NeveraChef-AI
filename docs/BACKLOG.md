# BACKLOG

Last updated: 2026-05-15
Source baseline: Notion backlog + Pencil + repo reality

## 1. Prioritization legend

- `P0`: critical for MVP path
- `P1`: high value, near-term
- `P2`: medium, can follow MVP core
- `P3`: post-MVP

Status:
- `todo`, `in_progress`, `done`, `blocked`

## 2. Sprint 0 - Organization

| Task | Type | Platform | Priority | Status | Acceptance criteria |
|---|---|---|---|---|---|
| Consolidate product docs in repo | Product/Docs | Shared | P0 | in_progress | Core docs exist and are coherent |
| Align Notion and repo context | Process | Shared | P0 | todo | Gaps and sync notes documented |
| Capture initial ADRs | Architecture | Shared | P0 | in_progress | Decisions and trade-offs recorded |

## 3. Sprint 1 - Design + architecture

| Task | Type | Platform | Priority | Status | Acceptance criteria |
|---|---|---|---|---|---|
| Define shared UI strategy (Compose Multiplatform) | Architecture | Android+iOS | P0 | done | Strategy documented with rationale |
| Define screen map from Pencil/Notion | UI/Product | Android+iOS | P0 | done | Screen list + flow + state model documented |
| Define design tokens and base components | UI | Android+iOS | P1 | in_progress | Token and component docs ready |

## 4. Sprint 2 - App mock foundation

| Task | Type | Platform | Priority | Status | Acceptance criteria |
|---|---|---|---|---|---|
| Implement navigation skeleton | Android+iOS UI | Shared UI | P0 | done | Navigation paths compile and run |
| Create placeholder screens (MVP set) | UI | Shared UI | P0 | done | Screen placeholders visible on both platforms |
| Add reusable Loading/Empty/Error states | UI | Shared UI | P1 | in_progress | Reusable state components integrated |
| Introduce domain models (pure Kotlin) | Domain | Shared | P0 | in_progress | Core models without platform deps |

## 5. Sprint 3 - AI text milestone

| Task | Type | Platform | Priority | Status | Acceptance criteria |
|---|---|---|---|---|---|
| Implement RecipePromptBuilder | AI | Shared | P0 | todo | Stable prompt from ingredients+preferences |
| Define recipe DTO schema + parser | Data/AI | Shared | P0 | todo | JSON contract parseable |
| Implement RecipeRepository + use case | Domain/Data | Shared | P0 | todo | Use case returns recipes or typed error |
| Connect UI to mocked then real AI pipeline | UI/Data | Shared UI | P1 | todo | Results screen consumes use case |

## 6. Sprint 4 - Product value expansion

| Task | Type | Platform | Priority | Status | Acceptance criteria |
|---|---|---|---|---|---|
| Shopping list flow | Feature | Shared UI | P1 | todo | Missing items can be added/checked |
| Preferences persistence | Feature | Shared | P1 | todo | Preferences survive app restart |
| Weekly menu prompt base | AI | Shared | P2 | todo | Weekly prompt draft + JSON contract |

## 7. Sprint 5 - Voice and vision

| Task | Type | Platform | Priority | Status | Acceptance criteria |
|---|---|---|---|---|---|
| Voice input pipeline | Platform + AI | Android+iOS | P2 | todo | Voice text enters review flow |
| Camera capture + detection pipeline | Platform + AI | Android+iOS | P2 | todo | Detected ingredients enter review flow |

## 8. Sprint 6 - Demo and hardening

| Task | Type | Platform | Priority | Status | Acceptance criteria |
|---|---|---|---|---|---|
| Demo script final | Product/Docs | Shared | P1 | todo | 5-7 min coherent technical demo |
| README + docs final cleanup | Docs | Shared | P1 | in_progress | Repo docs are complete and consistent |
| Q&A prep | Product | Shared | P2 | todo | High-risk questions covered |

## 9. Current blockers / open dependencies

1. AI provider final selection.
2. Final persistence milestone split.
3. Confirmation of weekly menu MVP status.
