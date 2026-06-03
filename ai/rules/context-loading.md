# Context Loading Rules

## Default Behaviour

- Do not scan the whole repository by default.
- Do not load skills by default.
- Do not load rules by default.
- Do not load architecture docs by default.
- Do not load validation docs by default.
- Do not open unrelated docs just because they exist.

## Required Order

1. User request.
2. Directly related code.
3. `AGENTS.md`.
4. The smallest relevant file under `ai/context/`, `ai/rules/` or `ai/skills/`.
5. Only then, deeper supporting docs if the task still needs them.

## Principle

Load the minimum context that can still solve the task correctly.
