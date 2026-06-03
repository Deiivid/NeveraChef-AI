# ARCH-02 Preparar AGENTS.md por feature

> Status: prepared
> Scope: repository instructions and AI docs structure

## Goal

Keep `AGENTS.md` small so Codex does not load unnecessary context by default.

## Result

- `AGENTS.md` is a lightweight entry point.
- Context, rules and skills are loaded only by trigger.
- Legacy root docs were removed after their canonical content moved to `ai/context/` and `ai/rules/`.
- Workflow records live in `ai/workflows/`.

## Context Loading Rule

Default order:

1. User request.
2. Direct target file.
3. Direct dependencies.
4. Optional docs only when the task needs them.

## Validation

Documentation-only. No Gradle validation required.
