# GitHub PR Workflow Skill

## Purpose

Use this skill when the user asks Codex to prepare commits, branches, pushes, pull requests, GitHub automation, or PR descriptions.

This skill is reusable across repositories.

## Activation

Use this skill only when the task explicitly involves:

- GitHub.
- Pull requests.
- Branch creation.
- Commit creation.
- Push operations.
- PR templates.
- GitHub CLI.
- Remote repository operations.

Do not use this skill for normal local code edits.

## Rules

- Use GitHub CLI only when explicitly requested.
- Do not create commits unless requested.
- Do not push unless requested.
- Do not open a PR unless requested.
- Do not force push.
- Do not rewrite history.
- Do not delete branches.
- Do not stage unrelated files.
- Do not stage secrets, local caches, build outputs, generated binaries, `.env` files, signing files, keystores, or tokens.
- Never expose secrets in PR descriptions, commits, logs, or summaries.

## Branch Rules

Use short descriptive branch names.

Preferred formats:

- feature/<short-description>
- fix/<short-description>
- refactor/<short-description>
- chore/<short-description>
- docs/<short-description>
- test/<short-description>

Use project-specific prefixes only if the repository already uses them.

Examples:

- feature/login-ui
- fix/recipe-detail-crash
- refactor/settings-state
- chore/update-ci-validation

## Commit Rules

When commits are requested:

- Keep commits atomic.
- Include only task-related files.
- Use semantic commit messages when possible.
- Do not commit if validation failed unless the user explicitly requests it.

Preferred commit format:

- feat(scope): short description
- fix(scope): short description
- refactor(scope): short description
- chore(scope): short description
- docs(scope): short description
- test(scope): short description

Examples:

- feat(ui): implement recipe detail layout
- fix(data): handle empty recipe response
- refactor(auth): simplify session state
- test(shared): add mapper coverage

## Validation Before PR

Before opening a PR:

- Run the most specific validation command available.
- Prefer existing project validation commands.
- Do not invent validation results.
- If validation fails, stop and report the failure.
- Do not open a PR with failing validation unless explicitly requested.

Common validation commands:

- ./gradlew test
- ./gradlew check
- ./gradlew lint
- ./gradlew detekt
- ./gradlew assembleDebug
- ./gradlew :app:assembleDebug
- ./gradlew :shared:compileKotlinAndroid
- ./gradlew :composeApp:assembleDebug

## PR Description

Keep PR descriptions short and factual.

Use this format:

Title: <type>: <short feature name>

Description:
- <what changed>
- <why it changed, only if useful>

Validation:
- <command run>

Risks:
- <real risks or None>

Notes:
- <optional, maximum 2 bullets>

## PR Safety Checklist

Before creating the PR, verify:

- No unrelated files are staged.
- No secrets are included.
- No local database/cache/build files are staged.
- No temporary TODO/FIXME/debug code was added.
- No Android-only imports leaked into KMP shared code.
- No public API was changed unless required.
- Validation result is real.

## Silent Mode

Do not narrate Git operations step by step.

Do not say:

- I will create a branch.
- I am checking git status.
- I am preparing the PR.
- I found these files.
- I will now push.

Only output when:

- A blocking question is required.
- The operation is blocked.
- The operation is complete.

Final output:

Done.

Branch:
- <branch-name>

Commit:
- <commit-hash or Not created>

PR:
- <url or Not created>

Validation:
- <command or Not run>

Risks:
- <real risks or None>