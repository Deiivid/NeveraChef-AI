# NeveraChef AI - Repository Map

## Top Level

```text
NeveraChefAI/
├── AGENTS.md
├── README.md
├── build.gradle.kts
├── settings.gradle.kts
├── ai/
├── androidApp/
├── iosApp/
└── shared/
```

## Main Areas

- `androidApp/` - Android host, entry points and Android-only integration.
- `iosApp/` - iOS host scaffold and Swift/iOS integration.
- `shared/` - Kotlin Multiplatform shared module.
- `ai/context/` - optional architecture, product, stack and repo background.
- `ai/rules/` - optional development, workflow, validation and policy rules.
- `ai/skills/` - optional task-specific execution skills.
- `ai/workflows/` - durable implementation records and architectural decisions.

## When To Open What

- Product semantics: `ai/context/product.md`
- Architecture boundaries: `ai/context/architecture.md`
- Stack decisions: `ai/context/stack.md`
- Execution policy: `ai/rules/README.md`
- Skills list: `ai/skills/README.md`

## Current Workflow Records

- `ai/workflows/architecture/ARCH-01-kmp-feature-layered-mvvm.md`
- `ai/workflows/ui/FOOD-DETAIL-SCREEN-2026-06-03.md`
