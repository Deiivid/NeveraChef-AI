# NOTION_SYNC

Last updated: 2026-05-15

## 1. Information confirmed from Notion

Main pages referenced:

- NeveraChef AI - Centro de Proyecto
- 01_Product_Vision.md
- 02_AI_Workflow.md
- 03_Technical_Architecture.md
- 04_Project_Workflow.md
- 05_Prompts.md
- 06_ADR_Decisions.md
- 09_Backlog_Summary.md
- 10_Local_First_UX_Onboarding.md
- Database: Backlog y Tareas

Confirmed decisions:

- Android-first MVP.
- Local-first no-login.
- AI text milestone before multimodal.
- Screen flow and backlog sprint model.

## 2. Information currently in repo docs

Implemented in `/docs`:

- Project context
- Product scope
- Architecture strategy
- UI screens inventory
- Design system baseline
- AI context
- Backlog
- Decisions
- NotebookLM context
- Notion sync status

## 3. Gaps between Notion and repo

1. Codebase still template-level while docs describe planned feature structure.
2. Notion has richer task granularity than current implementation.
3. Weekly menu positioning still appears mixed between MVP and future in different notes.

## 4. Pending sync actions

1. Confirm AI provider and record in Notion + `DECISIONS.md`.
2. Confirm persistence milestone split and reflect in backlog.
3. Align final MVP acceptance criteria wording across Notion and repo docs.
4. Ensure Pencil references (node IDs and latest exports) are periodically versioned.

## 5. Suggested Notion updates

1. Add a single "MVP locked scope" page to remove ambiguity.
2. Add a "Technical status vs plan" page linked to repo docs.
3. Add explicit field in backlog tasks: `Confirmed source` (Notion/Pencil/Code/Inference).

## 6. Working sync protocol

For each meaningful technical change:

1. Update repo docs first.
2. Update Notion summary page.
3. Log divergence and pending items here.
