# Development Conventions

## Scope

- Stay inside the requested scope.
- Touch only required files.
- Avoid unrelated refactors.
- Preserve existing naming, formatting and conventions.

## Change Discipline

- Make small, reviewable changes.
- Prefer simple, boring, production-grade solutions.
- Do not invent architecture, behavior or data contracts.
- Do not change public APIs unless the task requires it.
- Do not add dependencies, plugins or frameworks unless required and justified.

## Kotlin / Compose / KMP

- Keep business logic out of Composables.
- Keep `shared/commonMain` platform-agnostic.
- Use `expect/actual` only for real platform differences.
- Prefer existing design tokens and reusable components.
- Do not leak Android/iOS framework APIs into shared code.

## Safety

- Never expose secrets.
- Never claim validation that was not run.
- Do not change unrelated files to make the task easier.
