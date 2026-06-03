# NeveraChef AI - Stack

> **Scope:** Technical stack and defaults already chosen for the project.

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
| Secrets | Never commit or expose secrets. |

## Notes

- Keep the stack lean and consistent with the current repository.
- Do not upgrade core tooling unless the task explicitly asks for it.
