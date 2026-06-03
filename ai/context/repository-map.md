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

- `ai/workflows/README.md`
- `ai/workflows/architecture/ARCH-01-Definir-arquitectura-KMP-feature-layered-MVVM.md`
- `ai/workflows/architecture/ARCH-02-Preparar-AGENTS-md-por-feature.md`
- `ai/workflows/ui/UI-00-Preparar-referencias-y-workflow-Codex.md`
- `ai/workflows/ui/UI-02-Implementar-Inventario.md`
- `ai/workflows/ui/UI-03-Implementar-Lista-de-compra.md`
- `ai/workflows/ui/UI-04-Implementar-Ajustes-local-first.md`
- `ai/workflows/ui/UI-07-Implementar-Detalle-producto.md`
- `ai/workflows/ui/UI-08-Implementar-Anadir-producto.md`
