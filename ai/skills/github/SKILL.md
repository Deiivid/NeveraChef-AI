# GitHub PR Workflow Skill

Use this skill only when the user explicitly asks for branches, commits, pushes, pull requests, GitHub CLI, remote repository work or PR descriptions.

## Rules

- Do not create commits unless requested.
- Do not create branches unless requested.
- Do not push unless requested.
- Do not open PRs unless requested.
- Do not force push, rewrite history or delete branches unless explicitly requested.
- Do not stage unrelated files.
- Do not stage secrets, caches, build outputs, generated binaries, `.env` files, signing files, keystores or tokens.
- Never expose secrets in PR descriptions, commits, logs or summaries.

## Branches

Use short descriptive names. Prefer the repository convention if one exists.

Examples:

- `feature/login-ui`
- `fix/recipe-detail-crash`
- `refactor/settings-state`
- `docs/agent-instructions`

## Commits

When commits are requested:

- Keep commits atomic.
- Include only task-related files.
- Use semantic commit messages when practical.
- Do not commit with failing validation unless the user explicitly asks.

Examples:

- `feat(ui): implement recipe detail layout`
- `fix(data): handle empty recipe response`
- `refactor(settings): simplify state`
- `docs(ai): compact agent instructions`

## Validation Before PR

Before opening a PR:

1. Run the most specific validation command from `ai/VALIDATION_ANDROID.md`.
2. Stop if validation fails unless the user explicitly asks to continue.
3. Do not invent validation results.

## PR Description

```text
Title: <type>: <short feature name>

Description:
- <what changed>
- <why it changed, only if useful>

Validation:
- <command run>

Risks:
- <real risks or None>
```

## Safety Checklist

- No unrelated files staged.
- No secrets included.
- No local cache/build/generated files staged.
- No temporary debug code added.
- No Android-only imports leaked into shared code.
- No public API changed unless required.
- Validation result is real.

## Output

```text
Done.

Branch:
- <branch-name or Not created>

Commit:
- <commit-hash or Not created>

PR:
- <url or Not created>

Validation:
- <command or Not run>

Risks:
- <real risks or None>
```
